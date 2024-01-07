import {
  BadRequestException,
  ForbiddenException,
  Injectable,
  NotAcceptableException,
  NotFoundException,
} from '@nestjs/common';
import { PrismaService } from 'src/infra/database/prisma.service';
import { User } from '@prisma/client';
import { CreateUserRequest } from './dto/create-user-request';
import { randomUUID } from 'crypto';
import { UpdateUserRequest } from './dto/update-user-request';
import { ChangePasswordRequest } from './dto/change-password-request';
import { UserPasswordChangeRequest } from './dto/user-password-change-request';
import { PasswordService } from './password.service';
import { CreatePasswordRequest } from './dto/create-password-request';

@Injectable()
export class UserService {

  constructor(
    private readonly prismaService: PrismaService,
    private readonly passwordService: PasswordService
  ) {}


  /**
   * validar se 'novo' email ja existe na base de dados.
   * existindo, uma excecao sera lancada
   * @param email
   */
  private async validateCreateEmail(email: string) {
    const user = await this.findByEmail(email);
    if (user) {
      throw new BadRequestException('E-mail já existente');
    }
  }

  /**
   * validar se 'novo' login ja existe na base de dados.
   * existindo, uma excecao sera lancada
   * @param login 
   */
  private async validateCreateLogin(login: string) {
    const user = await this.findByLogin(login);
    if (user) {
      throw new BadRequestException('Login já existente');
    }
  }

  /**
   * criar um novo usuario
   * @param request 
   * @returns 
   */
  async create(request: CreateUserRequest) {
    const { name, email, login, profile } = request;

    await this.validateCreateEmail(email);
    await this.validateCreateLogin(login);

    return await this.prismaService.user.create({
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

  /**
   * pesquisar e retornar os dados de um usuario a partir de seu 'id'
   * @param value 
   * @returns 
   */
  async findById(value: string) {
    return await this.prismaService.user.findUnique({
      where: {
        id: value,
      },
    });
  }

  /**
   * pesquisar e retornar os dados de um usuario a partir de seu 'publickey'
   * @param value 
   * @returns 
   */
  async findByPublicKey(value: string) {
    return await this.prismaService.user.findUnique({
      where: {
        publickey: value,
      },
    });
  }

  /**
   * pesquisar e retornar os dados de um usuario a partir de seu 'login'
   * @param value 
   * @returns 
   */
  async findByLogin(value: string) {
    return await this.prismaService.user.findUnique({
      where: {
        login: value,
      },
    });
  }

  /**
   * pesquisar e retornar os dados de um usuario a partir de seu 'email'
   * @param value 
   * @returns 
   */
  async findByEmail(value: string) {
    return await this.prismaService.user.findUnique({
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

  /**
   * atualizar dados de um determinado usuário
   * - valida se 'id' informado existe para um usuario cadastrado na base e que esteja ativo
   * @param id
   * @param request 
   * @returns 
   */
  async update(id: string, request: UpdateUserRequest) {
    const user = await this.loadAndValidateActivity(id);

    const { name: nameRequest, profile: profileRequest } = request;

    return await this.prismaService.user.update({
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

  /**
   * tornar um usuário pré existente na base ativo
   * - caso o mesmo nao exista na base ou ja esteja ativo uma excecao sera lancada
   * @param id 
   * @returns 
   */
  async activate(id: string) {
    const user = await this.findById(id);

    if (!user) {
      throw new NotFoundException(`ID inválido.`);
    }

    if (user.active) {
      throw new NotAcceptableException('Usuário já está ativo');
    }

    return await this.prismaService.user.update({
      where: {
        id: id,
      },
      data: {
        active: true,
        lastUpdate: new Date(),
      },
    });
  }

  /**
   * tornar um usuário pré existente na base inativo
   * - caso o mesmo nao exista na base ou ja esteja inativo uma excecao sera lancada
   * @param id 
   * @returns 
   */
  async inactivate(id: string) {
    const user = await this.findById(id);

    if (!user) {
      throw new NotFoundException(`ID inválido.`);
    }

    if (!user.active) {
      throw new NotAcceptableException('Usuário já está inativo');
    }

    return await this.prismaService.user.update({
      where: {
        id: id,
      },
      data: {
        active: false,
        lastUpdate: new Date(),
      },
    });
  }

  /**
   * listar usuarios cadastrados
   * - podera efetuar a consulta incremental por parte do nome do usuario
   * - podera efetuar a consulta considerando apenas usuarios ativos ou inativos
   * @param name 
   * @param active 
   * @returns 
   */
  async list(name?: string, active?: boolean): Promise<User[]> {
    return await this.prismaService.user.findMany({
      where: {
        name: name ? { contains: name } : undefined,
        active: active,
      },
    });
  }

  /**
   * validar se senha atual e nova senha foram informadas
   * @param request 
   */
  private validateChangePasswordRequest(request: UserPasswordChangeRequest) {
    const requestCompleted = request
        && request.newpassword
        && request.password;

    if (!requestCompleted) {
        throw new BadRequestException('Requisição para alteração de senha é insuficiente!');
    }
}

  /**
   * realizar alteracao de senha do usuario
   * - valida se usuario informado existe e esta ativo, caso negativo lancara excecao
   * - validara senha atual
   * - validara nova senha com ultimas senhas usadas nas ultimas alteracoes conforme configuracao
   * - caso usuario ainda nao possua senha cadastrada utilizara o valor do email para validacao 
   *   como senha atual
   * @param userId 
   * @param request 
   */
  async changePassword(userId: string, request: UserPasswordChangeRequest) {
    console.log('\n\n> debug > userService.changePassword > userId, request -> ', userId, request);
    console.log('> debug > userService.changePassword > 1 > validateChangePasswordRequest');
    this.validateChangePasswordRequest(request);
    console.log('> debug > userService.changePassword > 2 > loadAndValidateActivity');
    const user = await this.loadAndValidateActivity(userId);
    console.log('> debug > userService.changePassword > 3 > hasPassword');
    const hasPassword = await this.passwordService.hasPassword(userId);
    if (hasPassword) {
      console.log('> debug > userService.changePassword > 3.1 > hasPassword > changePassword');
      await this.passwordService.changePassword(new ChangePasswordRequest(
        userId, 
        request.password, 
        request.newpassword));
    } else {
      console.log('> debug > userService.changePassword > 3.2 > sem senha cadastrada, validar email == request -> ', user.email, request.password);
      if (user.email !== request.password) {
        throw new ForbiddenException();
      }
      console.log('> debug > userService.changePassword > 3.2.1 > createPassword');
      await this.passwordService.createPassword(new CreatePasswordRequest(userId, request.newpassword));
    }
  }

}
