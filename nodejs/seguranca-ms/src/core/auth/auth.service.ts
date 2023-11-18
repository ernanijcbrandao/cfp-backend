import { Injectable } from '@nestjs/common';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class AuthService {
  constructor(private jwtService: JwtService) {}

  // fazer o login, buscando dados do user a partir do login e senha
  // aplicar as validacoes sobre a senha, atividade e possiveis bloqueios
  login(username: string, password: string) {
    const user = this.validateCredentials(username, password);

    const payload = {
      teste: 0 // username: user.login,
    };

    // montar o jwt a ser retornado
    return this.jwtService.sign(payload);
  }

  // buscar na base de dados um user para o username/login e password informados
  validateCredentials(username: string, password: string) {
    bcrypt.compareSync(password, '');
    // retornar o User encontrado
  }
}
