import { BadRequestException, ConflictException, Injectable } from '@nestjs/common';
import * as bcrypt from 'bcrypt';
import { env } from 'process';
import { RequestCreatePassword } from './dto/request-create-password';
import { PrismaService } from 'src/infra/database/prisma.service';
import { addDays } from 'date-fns';

@Injectable()
export class PasswordService {

    constructor(
        private prisma: PrismaService
    ) {}
      
    private hashPassword(password: string): Promise<string> {
        const saltRounds = 10;
        const salt = bcrypt.genSalt(saltRounds);
        const hashedPassword = bcrypt.hash(password, salt);
        return hashedPassword;
    }

    private comparePasswords(plainTextPassword: string, hashedPassword: string): Promise<boolean> {
        return bcrypt.compare(plainTextPassword, hashedPassword);
    }

    /**
     *  verificar se todos os parametros foram informados
     * @param request 
     */
    private validateRequestCreate(request: RequestCreatePassword) {
        const requestCompleted = request
            && request.profile
            && request.userId
            && request.password;

        if (!requestCompleted) {
            throw new BadRequestException('Requisição para criação de senha é insuficiente!');
        }
    }

    /**
     * valida se senha informada possui um tamanho minimo de 8 caracteres, uma letra maiuscula,
     * uma letra minuscula, um digito e caracteres especiais
     * @param request 
     */
    private validatePassword(request: RequestCreatePassword) {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_])[A-Za-z\d\W_]{8,}$/;
        const valid = regex.test(request.password);

        if (!valid) {
            throw new BadRequestException('A senha informada não possui os requisitos mínimos: possuir no mínimo 8 caracteres, letra maiúscula, minúscula, dígito e caracter(s) especial(is).');
        }
    }

    /**
     * contar o numero de senhas inativas armazenadas no historico para o usuario em questao
     * @param userId
     * @returns 
     */
    private async countPasswordsRegistered(request: RequestCreatePassword) {
        return this.prisma.password.count({
            where: {
                userId: request.userId,
                active: false,
              },
        });
    }

    /**
     * verificar se nova senha informada esta dentre as ultimas cadastradas
     * @param request 
     */
    private async checkIfPasswordHasAlreadyRegisteredInTheLatestUpdates(request: RequestCreatePassword) {
        const count = await this.prisma.password.count({
            where: {
                userId: request.userId,
                password: await this.hashPassword(request.password)
              },
        });
        if (count > 0) {
            throw new ConflictException('Senha informada já foi usada dentre as últimas ' 
                + env.NO_REPEAT_PASSWORD 
                + ' alterações realizadas.' );
        }
    }

    /**
     * inativar todas (pela regra havera apenas uma senha ativa) as senhas cadastradas para o 
     * usuario corrente
     * @param request 
     */
    private async disableCurrentPassword(request: RequestCreatePassword) {
        await this.prisma.password.updateMany({
            where: {
                userId: request.userId,
                active: true
            },
            data: {
                active: false
            },
        });
    }

    /**
     * gerar o parametro de data de expiracao da senha, considerando o parametro PASSWORD_EXPIRATION_TIME
     * configurado no arquivo .env. 
     * caso nao haja valor parametrizado ou o mesmo nao seja possivel converter em numero, sera considerado
     * valor -1
     * caso o valor parametrizado definido seja inferior a 1, nao havera data de expiracao de senha
     * sendo devolvido valor null. 
     * @returns 
     */
    private getExpireDate(): Date {
        const numberDaysExpirePassword = parseInt(process.env.PASSWORD_EXPIRATION_TIME, 10) || -1;
        return numberDaysExpirePassword > 0 
            ? addDays(new Date(), numberDaysExpirePassword) : null;
    }

    /**
     * adicionar em tabela a nova senha previamente validada
     * @param request
     * @returns 
     */
    private async addNewPassword(request: RequestCreatePassword) {
        return await this.prisma.password.create({
          data: {
            userId: request.userId,
            password: await this.hashPassword(request.password),
            expire: this.getExpireDate(),
            created: new Date(),
            active: true,
          },
        });
    }

    private async keepOnlyLastPasswordsasParameterized(request: RequestCreatePassword) {
        const numberDaysExpirePassword = parseInt(process.env.NO_REPEAT_PASSWORD, 10) || -1;
        const countPasswords = await this.countPasswordsRegistered(request);
    }
    
    /**
     * validar e gravar nova senha, mantendo ultimas senhas cadastradas conforme parametrizado
     * e apenas uma ativa para o usuario em questao
     * @param request
     */
    async createPassword(request: RequestCreatePassword) {
        this.validateRequestCreate(request);
        this.validatePassword(request);
        await this.checkIfPasswordHasAlreadyRegisteredInTheLatestUpdates(request);
        // TODO a seguir, implementar aqui o controle transacional, que com prisma eh feito manualmente
        await this.disableCurrentPassword(request);
        await this.addNewPassword(request);
    }

}
