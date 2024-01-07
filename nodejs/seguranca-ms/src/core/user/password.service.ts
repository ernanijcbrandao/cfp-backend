import { ForbiddenException, BadRequestException, ConflictException, Injectable, UnauthorizedException, InternalServerErrorException } from '@nestjs/common';
import * as bcrypt from 'bcrypt';
import { env } from 'process';
import { CreatePasswordRequest } from './dto/create-password-request';
import { PrismaService } from 'src/infra/database/prisma.service';
import { addDays, addMinutes, isAfter, isBefore } from 'date-fns';
import { ValidatePasswordRequest } from './dto/validate-password-request';
import { Password } from '@prisma/client';
import { ChangePasswordRequest } from './dto/change-password-request';
import { BlockService } from './block.service';
import { BlockReason } from './dto/block-reason.enum';
import { CreateTemporaryBlockRequest } from './dto/create-temporary-block-request';

@Injectable()
export class PasswordService {

    constructor(
        private prisma: PrismaService,
        private blocksService: BlockService
    ) {}

    /**
     * criptografar senha usando BCrypt
     * @param password 
     * @returns 
     */
    private async hashPassword(password: string): Promise<string> {
        const saltRounds = 10;
        const salt = await bcrypt.genSalt(saltRounds);
        const hashedPassword = await bcrypt.hash(password, salt);
        return hashedPassword;
    }

    /**
     * compara senhas
     * @param plainTextPassword 
     * @param hashedPassword 
     * @returns 
     */
    private async comparePasswords(plainTextPassword: string, hashedPassword: string): Promise<boolean> {
        const test = await bcrypt.compare(plainTextPassword, hashedPassword);
        return test;
    }

    /**
     *  verificar se todos os parametros foram informados
     * @param request 
     */
    private validateRequestCreate(request: CreatePasswordRequest) {
        const requestCompleted = request
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
    private validateMinimumRequirementsForPassword(password: string) {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_])[A-Za-z\d\W_]{8,}$/;
        const valid = regex.test(password);

        if (!valid) {
            throw new BadRequestException('A senha informada não possui os requisitos mínimos: possuir no mínimo 8 caracteres, letra maiúscula, minúscula, dígito e caracter(s) especial(is).');
        }
    }

    /**
     * contar o numero de senhas inativas armazenadas no historico para o usuario em questao
     * @param userId
     * @returns 
     */
    private async countPasswordsRegistered(request: CreatePasswordRequest) {
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
    private async checkIfPasswordHasAlreadyRegisteredInTheLatestUpdates(request: CreatePasswordRequest) {
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
    private async disableCurrentPassword(request: CreatePasswordRequest) {
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
    private async addNewPassword(request: CreatePasswordRequest) {
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

    /**
     * buscar a lista de historico de senhas na tabela ordenando pelo id de forma descendente,
     * assim retornando os registros dos mais novos para os mais antigos
     * conforme parametro historico (numero de senhas armazenadas), retornar o id do primeiro 
     * registro fora do limite informado
     * @param request 
     * @param historic 
     * @returns 
     */
    private async getIdFirstHistoryPasswordForDelete(request: CreatePasswordRequest, history: number) {
        const list = await this.prisma.password.findMany({
            where: {
              userId: request.userId,
              active: false,
            },
            orderBy: {
                id: 'desc',
            }
        });
        let result = -1;
        if ((list) && (list.length > history)) {
            result = list[history].id;
        }
        return result;
    }

    /**
     * apagar todos os registros de historico de senha que excederem o limite parametrizado 
     * de armazenagem de senhas. Para isso sera informado o id do primeiro registro, em ordem
     * decrescente, do qual sera o ponto de partida para a exclusao
     * @param request 
     * @param startId 
     */
    private async deleteExceededHistory(request: CreatePasswordRequest, startId: number) {
        await this.prisma.password.deleteMany({
            where: {
              userId: request.userId,
              active: false,
              id: {
                lte: startId
              }
            },
        });
    }

    /**
     * manter o historico de senhas conforme parametrizacao
     * @param request 
     * @returns 
     */
    private async keepOnlyLastPasswordsasParameterized(request: CreatePasswordRequest) {
        const countPasswords = await this.countPasswordsRegistered(request);
        if (countPasswords == 0) {
            return;
        }

        let keepHistoryPassword = parseInt(process.env.NO_REPEAT_PASSWORD, 10) || 0;
        keepHistoryPassword = keepHistoryPassword < 1 ? 0 : keepHistoryPassword;
        if (countPasswords <= keepHistoryPassword) {
            return;
        }

        const startId = await this.getIdFirstHistoryPasswordForDelete(request, keepHistoryPassword);
        await this.deleteExceededHistory(request, startId);
    }
    
    /**
     * validar e gravar nova senha, mantendo ultimas senhas cadastradas conforme parametrizado
     * e apenas uma ativa para o usuario em questao
     * @param request
     */
    async createPassword(request: CreatePasswordRequest) {
        console.log('> debug > createPassword > request -> ', request);
        console.log('> debug > 1 > validateRequestCreate ');
        this.validateRequestCreate(request);
        console.log('> debug > 2 > validateMinimumRequirementsForPassword ');
        this.validateMinimumRequirementsForPassword(request.password);
        console.log('> debug > 3 > checkIfPasswordHasAlreadyRegisteredInTheLatestUpdates ');
        await this.checkIfPasswordHasAlreadyRegisteredInTheLatestUpdates(request);
        // TODO a seguir, implementar aqui o controle transacional, que com prisma eh feito manualmente
        console.log('> debug > 4 > disableCurrentPassword ');
        await this.disableCurrentPassword(request);
        console.log('> debug > 5 > addNewPassword ');
        await this.addNewPassword(request);
        console.log('> debug > 6 > finalizado ');
    }

    /**
     *  verificar se todos os parametros foram informados
     * @param request 
     */
    private validateValidatePasswordRequest(request: ValidatePasswordRequest) {
        const requestCompleted = request
            && request.userId
            && request.password;

        if (!requestCompleted) {
            throw new BadRequestException('Requisição para validação de senha é insuficiente!');
        }
    }

    private async registerInvalidAccessDueToInvalidPassword(password: Password) {
        await this.prisma.password.update({
            where: {
                id: password.id
            },
            data: {
                invalidAttempt: password.invalidAttempt+1,
            },
        }).then(register => {
            const limitUnsuccessful = parseInt(process.env.LIMIT_UNSUCCESSFUL_ACCESS_ATTEMPTS, 10) || -1;
            const blockingExpirationTime = parseInt(process.env.PASSWORD_LOCK_EXPIRATION_TIME, 10) || 10;

            if ( (limitUnsuccessful > 0) && (register.invalidAttempt >= limitUnsuccessful)) {
                const requestBlock = new CreateTemporaryBlockRequest(password.userId,
                    BlockReason.TEMPORARY_BLOCKING_EXCEEDING_INCORRECT_PASSWORD_LIMIT,
                    addMinutes(new Date(), blockingExpirationTime)
                    );
                this.blocksService.createTemporaryBlocking(requestBlock);
            }
        }).catch(error => {
            throw new InternalServerErrorException();
        });
    }

    /**
     * recuperar a senha ativa (unica) para o usuario em questao
     * @param request 
     * @returns 
     */
    private async getPasswordActive(request: ValidatePasswordRequest | ChangePasswordRequest ): Promise<Password> {
        console.log('\n\n> debug > PasswordService.getPasswordActive > request -> ', request);
        console.log('> debug > PasswordService.getPasswordActive > 1 > consultar na base por senhas ativas para o userId ');
        const password = await this.prisma.password.findFirst({
            where: {
              userId: request.userId,
              active: true,
            },
        });

        console.log('> debug > PasswordService.getPasswordActive > 2 > result -> ', password);
        if (!password) {
            console.log('> debug > PasswordService.getPasswordActive > 2.1 > nao ha result -> forbiden ');
            throw new ForbiddenException();
        }

        const valid = this.comparePasswords(request.password, password.password);
        console.log('> debug > PasswordService.getPasswordActive > 3 > compare passwords request x base -> ', valid);
        if (!valid) {
            console.log('> debug > PasswordService.getPasswordActive > 3.1 > compare false > registrar tentativa invalida');
            console.log('> debug > PasswordService.getPasswordActive > 3.1.1 > registerInvalidAccessDueToInvalidPassword');
            this.registerInvalidAccessDueToInvalidPassword(password);
            throw new UnauthorizedException('Senha inválida!');
        }

        console.log('> debug > PasswordService.getPasswordActive > 4 > retornar password encontrada');
        console.log('\n\n');
        return password;
    }

    /**
     * retornar true caso exista alguma senha cadastrada para usuario
     * , do contrario false
     * @param userId 
     * @returns 
     */
    async hasPassword(userId: string): Promise<boolean> {
        console.log('\n\n> debug > PasswordSerice.hasPassword > userId -> ', userId);
        console.log('> debug > PasswordSerice.hasPassword > 1 > contar senhas cadastradas para userId ');
        const count = await this.prisma.password.count({
            where: {
              userId: userId
            },
        });
        console.log('> debug > PasswordSerice.hasPassword > 2 > count -> ', count, (count>0));
        console.log('\n\n');
        return count > 0;
    }

    /**
     * verificar se senha esta expirada. 
     * em caso positivo lancar excecao 
     * @param password 
     * @returns 
     */
    private async checkExpiredPassword(password: Password) {
        if ((!password.expire) || isAfter(password.expire, new Date())) {
            return;
        }
        throw new UnauthorizedException('Senha expirada!');
    }

    private async registerValidAccess(password: Password) {
        await this.prisma.password.update({
            where: {
                id: password.id
            },
            data: {
                invalidAttempt: 0,
            },
        });
    }

    /**
     * validar senha de um detemrinado usuario
     * caso a senha seja invalida um Forbidden sera lancado
     * caso esta senha esteja expirada um Unauthorized sera lancado
     * @param request 
     */
    async validatePassword(request: ValidatePasswordRequest) {
        this.validateValidatePasswordRequest(request);
        const password = await this.getPasswordActive(request);
        this.checkExpiredPassword(password);
        this.registerValidAccess(password);
    }

    /**
     *  verificar se todos os parametros foram informados
     * @param request 
     */
    private validateChangePasswordRequest(request: ChangePasswordRequest) {
        const requestCompleted = request
            && request.userId
            && request.password
            && request.newpassword;

        if (!requestCompleted) {
            throw new BadRequestException('Requisição para alteração de senha é insuficiente!');
        }
    }

    /**
     * altera a senha atual de um detemrinado usuario
     * valida se a senha atual informada eh valida
     * valida se nova senha possui requisitos minimos aceitaveis
     * cria a nova senha
     * inativa a senha anterior, mantendo o controle historico conforme parametrizado
     * @param request 
     */
    async changePassword(request: ChangePasswordRequest) {
        console.log('\n\n> debug > PasswordService.changePassword > request -> ', request);
        console.log('> debug > PasswordService.changePassword > 1 > validateChangePasswordRequest');
        this.validateChangePasswordRequest(request);
        console.log('> debug > PasswordService.changePassword > 2 > getPasswordActive');
        this.getPasswordActive(request);
        // TODO demais validacoes de senha atual com senha recuperada e nova senha
        // nao podendo ser senha ja existente no historico
        const requestNewPassword = {
            userId: request.userId,
            password: request.newpassword
        }
        this.createPassword(requestNewPassword);
    }

}
