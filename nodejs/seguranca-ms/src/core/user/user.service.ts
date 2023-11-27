import {
  BadRequestException,
  Injectable,
  NotAcceptableException,
  NotFoundException,
} from '@nestjs/common';
import { PrismaService } from 'src/infra/database/prisma.service';
import { User } from '@prisma/client';
import { RequestCreateUser } from './dto/request-create-user';
import { randomUUID } from 'crypto';
import { RequestUpdateUser } from './dto/request-update-user';
import { UserProfile } from './dto/user-profile.enum';
import { RequestChangePassword } from '../password/dto/request-change-password';
import { UserPasswordChangeRequest } from './dto/user-password-change-request';

@Injectable()
export class UserService {

  constructor(
    private prisma: PrismaService
  ) {}

  async create(request: RequestCreateUser) {
    const { name, email, login, profile } = request;

    await this.validateEmail(email);
    await this.validateLogin(login);

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

  async findByEmail(value: string) {
    return await this.prisma.user.findUnique({
      where: {
        email: value,
      },
    });
  }

  private async loadAndValidateActivity(userId: string): Promise<User> {
    const user = await this.findById(userId);

    if (!user) {
      throw new NotFoundException(`ID inválido.`);
    }

    if (!user.active) {
      throw new NotAcceptableException('Usuário inativo');
    }

    return user;
  }

  async update(id: string, request: RequestUpdateUser) {
    const user = await this.loadAndValidateActivity(id);

    const { name: nameRequest, profile: profileRequest } = request;

    return await this.prisma.user.update({
      where: {
        id: id,
      },
      data: {
        name: !nameRequest ? user.name : nameRequest,
        profile: !profileRequest ? user.profile : profileRequest,
        lastUpdate: new Date(),
      },
    });
  }

  async active(id: string) {
    const user = await this.findById(id);

    if (!user) {
      throw new NotFoundException(`ID inválido.`);
    }

    if (user.active) {
      throw new NotAcceptableException('Usuário já está ativo');
    }

    return await this.prisma.user.update({
      where: {
        id: id,
      },
      data: {
        active: true,
        lastUpdate: new Date(),
      },
    });
  }

  async inactive(id: string) {
    const user = await this.findById(id);

    if (!user) {
      throw new NotFoundException(`ID inválido.`);
    }

    if (!user.active) {
      throw new NotAcceptableException('Usuário já está inativo');
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

  async list(name?: string, active?: boolean): Promise<User[]> {
    return await this.prisma.user.findMany({
      where: {
        name: name ? { contains: name } : undefined,
        active: active,
      },
    });
  }

  async validateEmail(email: string) {
    const user = await this.findByEmail(email);
    if (user) {
      throw new BadRequestException('E-mail já existente');
    }
  }

  async validateLogin(login: string) {
    const user = await this.findByLogin(login);
    if (user) {
      throw new BadRequestException('Login já existente');      
    }
  }

  async changePassword(userId: string, request: UserPasswordChangeRequest) {
    const user = this.loadAndValidateActivity(userId);

    // return await this.prisma.user.update({
    //   where: {
    //     id: id,
    //   },
    //   data: {
    //     name: !nameRequest ? user.name : nameRequest,
    //     profile: !profileRequest ? user.profile : profileRequest,
    //     lastUpdate: new Date(),
    //   },
    // });
  }

}
