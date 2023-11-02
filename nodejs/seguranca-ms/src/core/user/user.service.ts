import {
  Injectable,
  NotAcceptableException,
  NotFoundException,
} from '@nestjs/common';
import { PrismaService } from 'src/infra/database/prisma.service';
import { User } from '@prisma/client';
import { RequestCreateUser } from './dto/request-create-user';
import { randomUUID } from 'crypto';
import { DecimalUtilsService } from 'src/util/decimal-utils-service';
import { RequestUpdateUser } from './dto/request-update-user';
import { UserProfile } from './dto/user-profile.enum';

@Injectable()
export class UserService {
  constructor(
    private prisma: PrismaService,
    private decimalUtils: DecimalUtilsService,
  ) {}

  async create(request: RequestCreateUser) {
    const { name, email, login, profile } = request;

    return await this.prisma.user.create({
      data: {
        id: randomUUID(),
        publickey: randomUUID(),
        name,
        email,
        login,
        profile,
        created: new Date(),
        active: true,
      },
    });
  }

  async findById(value: string) {
    return await this.prisma.user.findUnique({
      where: {
        id: value,
      },
    });
  }

  async findByPublicKey(value: string) {
    return await this.prisma.user.findUnique({
      where: {
        publickey: value,
      },
    });
  }

  async findByLogin(value: string) {
    return await this.prisma.user.findUnique({
      where: {
        login: value,
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
      throw new NotAcceptableException('Este usuário encontra-se inativo');
    }

    const { name, email, profile } = request;

    return await this.prisma.user.update({
      where: {
        id: id,
      },
      data: {
        name: !name ? user.name : name,
        email: !email ? user.email : email,
        profile: !profile ? user.profile : profile,
        lastUpdate: new Date(),
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
        lastUpdate: new Date(),
      },
    });
  }

  async list(name?: string, profile?: UserProfile): Promise<User[]> {
    return await this.prisma.user.findMany({
      where: {
        name: name ? { contains: name } : undefined,
        profile: profile ? profile : undefined,
        active: true,
      },
    });
  }
}
