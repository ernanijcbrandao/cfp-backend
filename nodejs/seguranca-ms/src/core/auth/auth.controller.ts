import { Body, Controller, Get, Post, UseGuards } from '@nestjs/common';
import { AuthGuardion } from './auth.guardion';
import { AuthService } from './auth.service';
import LoginRequest from './dto/login-request';
import { Role } from './role.decorator';
import { RoleGuard } from './role.guard';

@Controller('security')
export class AuthController {
  constructor(private authService: AuthService) {}

  @Post('authenticate')
  authenticate(@Body() body) {
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
