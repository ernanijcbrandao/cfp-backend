import { Body, Controller, Get, Post, UseGuards } from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';
import { AuthGuardion } from './auth.guardion';
import { AuthService } from './auth.service';
import AuthenticateRequest from './dto/authenticate-request';
import { Role } from './role.decorator';
import { RoleGuard } from './role.guard';

@ApiTags('security')
@Controller('authenticate')
export class AuthController {
  constructor(private authService: AuthService) {}

  @Post()
  async authenticate(@Body() body: AuthenticateRequest) {
    const result = await this.authService.authenticate(body);
    console.log(result);
    return result;
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
