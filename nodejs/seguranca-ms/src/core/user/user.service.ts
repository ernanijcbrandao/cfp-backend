import { BadRequestException, ConflictException, Injectable, NotAcceptableException, NotFoundException } from '@nestjs/common';
import { PrismaService } from 'src/infra/database/prisma.service';
import { User, Prisma } from '@prisma/client';
import { RequestCreateUser } from './dto/request-create-user';
import { randomUUID } from 'crypto';
import { DecimalUtilsService } from 'src/util/decimal-utils-service';
import { RequestUpdateUser } from './dto/request-update-use';

@Injectable()
export class UserService {

  constructor(private prisma: PrismaService, private decimalUtils: DecimalUtilsService) {}

  async create(request: RequestCreateUser) {

    const { name, description, owner, openingBalance: requestOpeningBalance } = request;

    return await this.prisma.user.create({
      data: {
          id: randomUUID(),
          name,
          description,
          owner,
          active: true
      }
    });

  }

  async findById(id: string) {
    return await this.prisma.user.findUnique({
      where: {
        id,
      },
    });
  }

  async findByName(name: string) {
    return await this.prisma.user.findUnique({
      where: {
          name,
      },
    });
  }

  async update(id: string, request: RequestUpdateUser) {

    const user = await this.prisma.user.findUnique({
      where: {
        id: id,
      },
    });

    if (!user) {
      throw new NotFoundException(`ID informado é inválido.`);
    }

    if (!user.active) {
      throw new NotAcceptableException("Este usuário encontra-se inativa");
    }

    const { name, description, owner, openingBalance: requestOpeningBalance } = request;

    if (name && user.name !== name) {
      const username = await this.prisma.user.findUnique({
        where: {
          name,
        },
      });
      if (username) {
        throw new ConflictException(`O nome da conta informada para alteração \'${name}\'`);
      }
    }

    return await this.prisma.user.update({
      where: {
        id: id,
      },
      data: {
          name: (!name ? user.name : name ),
          lastUpdate: new Date()
      }
    });

  }

  async delete(id: string) {

    const user = await this.prisma.user.findUnique({
      where: {
        id: id,
      },
    });

    if (!user) {
      throw new NotFoundException(`ID informado é inválido.`);
    }

    // TODO validar se existem movimentacoes atreladas a conta. havendo negar exclusao

    await this.prisma.user.delete({
      where: {
        id: id,
      },
    });

  }

  async inactive(id: string) {

    const user = await this.prisma.user.findUnique({
      where: {
        id: id,
      },
    });

    if (!user) {
      throw new NotFoundException(`ID informado é inválido.`);
    }

    return await this.prisma.user.update({
      where: {
        id: id,
      },
      data: {
          active: false,
          lastUpdate: new Date()
      }
    });

  }

  async load(id: string) {

    const user = await this.prisma.user.findUnique({
      where: {
        id: id,
      },
    });

    if (!user) {
      throw new NotFoundException(`ID informado é inválido.`);
    }

    return user;

  }

  async list(name?: string): Promise<User[]> {

    return await this.prisma.user.findMany({
      where: {
        name: name ? {contains: name} : undefined,
        active: true
      },
    });

  }

}
