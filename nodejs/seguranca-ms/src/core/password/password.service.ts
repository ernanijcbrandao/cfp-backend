import { ForbiddenException, BadRequestException, ConflictException, Injectable, UnauthorizedException, InternalServerErrorException } from '@nestjs/common';
import * as bcrypt from 'bcrypt';
import { env } from 'process';
import { RequestCreatePassword } from './dto/request-create-password';
import { PrismaService } from 'src/infra/database/prisma.service';
import { addDays, addMinutes, isAfter, isBefore } from 'date-fns';
import { RequestValidatePassword } from './dto/request-validate-password';
import { Password } from '@prisma/client';
import { RequestChangePassword } from './dto/request-change-password';
import { BlockService } from '../block/block.service';
import { BlockReason } from '../block/dto/block-reason.enum';
import { RequestCreateTemporaryBlock } from '../block/dto/request-create-temporary-block';

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
    private hashPassword(password: string): Promise<string> {
        const saltRounds = 10;
        const salt = bcrypt.genSalt(saltRounds);
        const hashedPassword = bcrypt.hash(password, salt);
        return hashedPassword;
    }

    private async comparePasswords(plainTextPassword: string, hashedPassword: string): Promise<boolean> {
        return bcrypt.compare(plainTextPassword, hashedPassword);
    }

    /**
     *  verificar se todos os parametros foram informados
     * @param request 
     */
    private validateRequestCreate(request: RequestCreatePassword) {
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

    /**
     * buscar a lista de historico de senhas na tabela ordenando pelo id de forma descendente,
     * assim retornando os registros dos mais novos para os mais antigos
     * conforme parametro historico (numero de senhas armazenadas), retornar o id do primeiro 
     * registro fora do limite informado
     * @param request 
     * @param historic 
     * @returns 
     */
    private async getIdFirstHistoryPasswordForDelete(request: RequestCreatePassword, history: number) {
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
    private async deleteExceededHistory(request: RequestCreatePassword, startId: number) {
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
    private async keepOnlyLastPasswordsasParameterized(request: RequestCreatePassword) {
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
    async createPassword(request: RequestCreatePassword) {
        this.validateRequestCreate(request);
        this.validateMinimumRequirementsForPassword(request.password);
        await this.checkIfPasswordHasAlreadyRegisteredInTheLatestUpdates(request);
        // TODO a seguir, implementar aqui o controle transacional, que com prisma eh feito manualmente
        await this.disableCurrentPassword(request);
        await this.addNewPassword(request);
    }

    /**
     *  verificar se todos os parametros foram informados
     * @param request 
     */
    private validateRequestValidatePassword(request: RequestValidatePassword) {
        const requestCompleted = request
            && request.userId
            && request.password;

        if (!requestCompleted) {
            throw new BadRequestException('Requisição para validação de senha é insuficiente!');
        }
    }

    private async registerInvalidAccessDueToInvalidPassword(password: Password) {
        const register = await this.prisma.password.update({
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
                const requestBlock = new RequestCreateTemporaryBlock(password.userId,
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
    private getPasswordActive(request: RequestValidatePassword | RequestChangePassword ): Password {
        this.prisma.password.findFirst({
            where: {
              userId: request.userId,              
              active: true,
            },
        }).then(password => {
            if (!password) {
                throw new UnauthorizedException('Usuário inválido!');
            }
            this.comparePasswords(request.password, password.password).then(valid => {
                if (!valid) {
                    this.registerInvalidAccessDueToInvalidPassword(password);
                    throw new UnauthorizedException('Senha inválida!');
                }
            });
            return password;
        }).catch(error => {
            throw new InternalServerErrorException();
        });
        return null;
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
    async validatePassword(request: RequestValidatePassword) {
        this.validateRequestValidatePassword(request);
        const password = this.getPasswordActive(request);
        this.checkExpiredPassword(password);
        this.registerValidAccess(password);
    }

    /**
     *  verificar se todos os parametros foram informados
     * @param request 
     */
    private validateRequestChangePassword(request: RequestChangePassword) {
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
    async changePassword(request: RequestChangePassword) {
        this.validateRequestChangePassword(request);
        this.getPasswordActive(request);
        const requestNewPassword = {
            userId: request.userId,
            password: request.newpassword
        }
        this.createPassword(requestNewPassword);
    }

}
