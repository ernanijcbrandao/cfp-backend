import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { env } from 'process';
import { PrismaService } from 'src/infra/database/prisma.service';
import { JwtStrategyService } from '../jwt-strategy/jwt-strategy.service';
import { BlockService } from '../user/block.service';
import { PasswordService } from '../user/password.service';
import { UserService } from '../user/user.service';
import { AuthController } from './auth.controller';
import { AuthService } from './auth.service';

@Module({
  imports: [
    JwtModule.register({
      secret: env.SECRET_PHRASE,
      signOptions: {
        expiresIn: env.TIMEOUT_TOKEN,
      },
    }),
  ],
  controllers: [AuthController],
  providers: [AuthService, 
    UserService, 
    BlockService, 
    PasswordService,
    PrismaService,
    JwtStrategyService
  ],
})
export class AuthModule {}
