import { BadRequestException, Injectable } from '@nestjs/common';
import { PrismaService } from 'src/infra/database/prisma.service';
import { CreateUserSystemAuthorizationRequest } from './dto/create-user-system-authorization-request';
import { UserService } from './user.service';

@Injectable()
export class SystemsService {

    constructor(
        private readonly prismaService: PrismaService,
        private readonly userService: UserService
      ) {}
    
    private async validateCreateAuthorization(request: CreateUserSystemAuthorizationRequest) {
        const valid = (request)
                && (request.userId)
                && (request.code)
                && (request.name);

        if (!valid) {
            throw new BadRequestException('Requisição para Autorização usuário x sistema é insuficiente!');
        }
    }

    private async validateUserAuthorizationRequest(userId: string) {
        let valid = false;

        if (userId) {
            const user = await this.userService.findById(userId);
            valid = (user) && (user.active);
        }

        if (!valid) {
            throw new BadRequestException('Usuário inválido');
        }
    }

    private async validateUserSystemAuthorizationRequest(userId: string, systemCode: string) {
        const count = await this.prismaService.systemAuthorization.count({
            where : {
                userId: userId,
                code: systemCode,
            },
        });

        if (count > 0) {
            throw new BadRequestException('Solicitação de autorização usuário x sistema inválida');
        }
    }

    /**
     * criar uma nova autorizacao usuario x sistema
     * @param request 
     * @returns 
     */
    async create(request: CreateUserSystemAuthorizationRequest) {
        await this.validateCreateAuthorization(request);
        await this.validateUserAuthorizationRequest(request.userId);
        await this.validateUserSystemAuthorizationRequest(request.userId, request.code);
        const { userId, code, name } = request;

        return await this.prismaService.systemAuthorization.create({
            data: {
                userId,
                name,
                code,
                created: new Date(),
                active: true,
            },
        });
    }

    /**
     * verifica se ja autorizacao de usuario para um determinado sistema (code)
     * @param value 
     * @returns 
     */
    async authorized(userId: string, code: string): Promise<boolean> {
        const count = await this.prismaService.systemAuthorization.count({
            where: {
                userId: userId,
                code: code,
                active: true,
            },
        });

        return count > 0;
    }

    
}
