import { BadRequestException, Injectable, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { User } from '@prisma/client';
import { BlockService } from '../user/block.service';
import { ValidatePasswordRequest } from '../user/dto/validate-password-request';
import { PasswordService } from '../user/password.service';
import { UserService } from '../user/user.service';
import AuthenticateRequest from './dto/authenticate-request';

@Injectable()
export class AuthService {
  constructor(
    private jwtService: JwtService,
    private userService: UserService,
    private blockService: BlockService,
    private passwordService: PasswordService
    ) {
  }

  /**
   * validar os dados obrigatorios da requisicao recebida
   * @param request 
   */
  private validateAuthenticateRequest(request: AuthenticateRequest) {
    const requestCompleted = request
        && request.username
        && request.password;

    if (!requestCompleted) {
        throw new BadRequestException('Dados para autenticação insuficientes!');
    }
  }

  private async checkForBlockages(userId: string) {
    const block = await this.blockService.getBlock(userId);
    if (block) {
      throw new UnauthorizedException(block.description); 
    }
  }

  /**
   * validar acesso para username / password informados
   * - procurar e recuperar dados do usuario para o username informado
   * - validar veracidade da senha, atividade da mesma
   * - validar possiveis bloqueios
   * 
   * @param request 
   */
  private async validateCredentialsAndGeneratePayloadJWT(request: AuthenticateRequest) {
    const user = await this.userService.loadAndValidateActivityByLogin(request.username);
    await this.passwordService.validateCurrentPassword(new ValidatePasswordRequest(user.id, request.password));
    await this.checkForBlockages(user.id);
    // gerar payload pra retornar
    return this.createPayload(user);
  }

  private createPayload(user: User) {
    const payload = {
      suject: user.publickey,
      name: user.name,
      profile: user.profile,
    }

    return payload;
  }

  async authenticate(request: AuthenticateRequest) {
    this.validateAuthenticateRequest(request);
    const payload = await this.validateCredentialsAndGeneratePayloadJWT(request);    
    // console.log('> DEBUG > Payload -> ', payload);
    // gerar e retornar token para o payload gerado
    // const payload = {
    //   teste: 'teste 1'
    // };
    return {
      token: this.jwtService.sign(payload)
    };
  }

}
