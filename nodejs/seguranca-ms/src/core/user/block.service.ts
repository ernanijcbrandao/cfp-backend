import { BadRequestException, Injectable } from '@nestjs/common';
import { PrismaService } from 'src/infra/database/prisma.service';
import { CreateTemporaryBlockRequest } from './dto/create-temporary-block-request';
import { isAfter } from 'date-fns';

@Injectable()
export class BlockService {

    constructor(
        private prisma: PrismaService
    ) {}
    
    /**
     * sempre nas requisicoes a este servico, varrer e atualizar os bloqueios temporarios
     * de maneira a inativa-los com assim expirados
     */
    private async disableExpiredLocks() {
        this.prisma.blocks.updateMany({
            where: {
                active: true,
                expire: {
                    not: null,
                    lt: new Date()
                }
            },
            data: {
                active: false
            },
        });
    }

    private validateCreateTemporaryBlockRequest(request: CreateTemporaryBlockRequest) {
        const requestCompleted = request
            && request.userId
            && request.reason
            && request.expire
            && isAfter(request.expire, new Date());

        if (!requestCompleted) {
            throw new BadRequestException('Requisição para criação de bloqueio temporário é insuficiente!');
        }
    }

    private async create(request: CreateTemporaryBlockRequest) {
        return await this.prisma.blocks.create({
            data: {
              userId: request.userId,
              reason: request.reason,
              description: request.description,
              expire: request.expire,
              active: true,
            },
        });
    }

    async createTemporaryBlocking(request: CreateTemporaryBlockRequest) {
        await this.disableExpiredLocks();
        this.validateCreateTemporaryBlockRequest(request);
        await this.create(request);
    }

    async getBlock(userId: string) {
        await this.disableExpiredLocks();
        return this.prisma.blocks.findFirst({
            where: {
                userId: userId,
                active: true
            },
        });
    }

}
