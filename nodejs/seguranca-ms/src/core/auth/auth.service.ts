import { Injectable, InternalServerErrorException, UnauthorizedException } from '@nestjs/common';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';
import { PasswordService } from '../password/password.service';
import { ValidatePasswordRequest } from '../password/dto/validate-password-request';
import { UserService } from '../user/user.service';
import LoginRequest from './dto/login-request';
import { User } from '@prisma/client';
import { BlockService } from '../block/block.service';

@Injectable()
export class AuthService {
  constructor(
    private jwtService: JwtService,
    private userService: UserService,
    private blockService: BlockService,
    private passwordService: PasswordService
    ) {}

  // fazer o login, buscando dados do user a partir do login e senha
  // aplicar as validacoes sobre a senha, atividade e possiveis bloqueios
  login(request: LoginRequest) {
    const user = this.validateCredentials(request);

    const payload = {
      teste: 0 // username: user.login,
    };

    // montar o jwt a ser retornado
    return this.jwtService.sign(payload);
  }

  private validateLocks(user: User) {
    this.blockService.getBlock(user.id).then(block => {
      if (block) {
        throw new UnauthorizedException('Bloqueio ativo para este usuário!');
      }
    }).catch(error => {
      throw new InternalServerErrorException();
    });
  }

  private validatePassword(request: ValidatePasswordRequest) {
    this.passwordService.validatePassword(request);
  }

  private validateUser(request: LoginRequest): User {
    this.userService.findById(request.login).then(user => {
      if (!user) {
        throw new UnauthorizedException('Usuário inválido!');
      }
      if (!user.active) {
        throw new UnauthorizedException('Usuário inativo!');
      }

      this.validateLocks(user);
      this.validatePassword(new ValidatePasswordRequest(user.id, request.password));

      return user;
    }).catch(error => {
      throw new InternalServerErrorException();
    });

    return null;
  }

  // buscar na base de dados um user para o username/login e password informados
  private async validateCredentials(request: LoginRequest) {
    const user = this.validateUser(request);
    // TODO criar e devolver token
  }
  
}
