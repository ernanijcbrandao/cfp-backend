import { Body, Controller, Get, Post, UseGuards } from '@nestjs/common';
import { AuthService } from './auth.service';
import { AuthGuardion } from './auth.guardion';
import { Role } from './role.decorator';
import { RoleGuard } from './role.guard';
import LoginRequest from './dto/login-request';

@Controller('auth')
export class AuthController {
  constructor(private authService: AuthService) {}

  @Post('autenticacao')
  autenticar(@Body() body) {
    return this.authService.login(new LoginRequest(body.username, body.password));
  }

  @Role('admin')
  @UseGuards(AuthGuardion, RoleGuard)
  @Get('testeUrlProtegida')
  validateProtectedURL() {
    return {
      mensagem: 'Teste URL Protegida',
    };
  }
}
