import { BadRequestException, ConflictException, Injectable, NotAcceptableException, NotFoundException } from '@nestjs/common';
import { PrismaService } from 'src/infra/database/prisma.service';
import { Account, Prisma } from '@prisma/client';
import { RequestCreateAccount } from './dto/request-create-account';
import { randomUUID } from 'crypto';
import { DecimalUtilsService } from 'src/util/decimal-utils-service';
import { RequestUpdateAccount } from './dto/request-update-account';

@Injectable()
export class AccountService {

  constructor(private prisma: PrismaService, private decimalUtils: DecimalUtilsService) {}

  async create(request: RequestCreateAccount) {

    const { name, description, owner, openingBalance: requestOpeningBalance } = request;

    return await this.prisma.account.create({
      data: {
          id: randomUUID(),
          name,
          description,
          owner,
          openingBalance: this.decimalUtils.convertDecimalToStringWithTwoDecimals(requestOpeningBalance),
          active: true
      }
    });

  }

  async findById(id: string) {
    return await this.prisma.account.findUnique({
      where: {
        id,
      },
    });
  }

  async findByName(name: string, owner: string) {
    return await this.prisma.account.findUnique({
      where: {
        nameAccountUniqueForOwner: {
          name,
          owner,
        },
      },
    });
  }

  async update(id: string, request: RequestUpdateAccount) {

    const account = await this.prisma.account.findUnique({
      where: {
        id: id,
      },
    });

    if (!account) {
      throw new NotFoundException(`ID informado é inválido.`);
    }

    if (!account.active) {
      throw new NotAcceptableException("Esta conta encontra-se inativa");
    }

    const { name, description, owner, openingBalance: requestOpeningBalance } = request;

    if ((name && account.name !== name) || (owner && owner !== account.owner)) {
      const accountname = await this.prisma.account.findUnique({
        where: {
          nameAccountUniqueForOwner: {
            name,
            owner,
          },
        },
      });
      if (accountname) {
        throw new ConflictException(`O nome da conta informada para alteração \'${name}\' já está cadastrado para outra conta de mesmo dono \'${owner}\'.`);
      }
    }

    return await this.prisma.account.update({
      where: {
        id: id,
      },
      data: {
          name: (!name ? account.name : name ),
          description: (!description ? account.description : description),
          owner: (!owner ? account.owner : owner),
          openingBalance: (!requestOpeningBalance ? account.openingBalance : this.decimalUtils.convertDecimalToStringWithTwoDecimals(requestOpeningBalance)),
          lastUpdate: new Date()
      }
    });

  }

  async delete(id: string) {

    const account = await this.prisma.account.findUnique({
      where: {
        id: id,
      },
    });

    if (!account) {
      throw new NotFoundException(`ID informado é inválido.`);
    }

    // TODO validar se existem movimentacoes atreladas a conta. havendo negar exclusao

    await this.prisma.account.delete({
      where: {
        id: id,
      },
    });

  }

  async inactive(id: string) {

    const account = await this.prisma.account.findUnique({
      where: {
        id: id,
      },
    });

    if (!account) {
      throw new NotFoundException(`ID informado é inválido.`);
    }

    return await this.prisma.account.update({
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

    const account = await this.prisma.account.findUnique({
      where: {
        id: id,
      },
    });

    if (!account) {
      throw new NotFoundException(`ID informado é inválido.`);
    }

    return account;

  }

  async list(owner: string, name?: string): Promise<Account[]> {

    return await this.prisma.account.findMany({
      where: {
        owner: owner,
        name: name ? {contains: name} : undefined,
        active: true
      },
    });

  }

}
