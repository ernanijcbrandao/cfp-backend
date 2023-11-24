import { Module } from '@nestjs/common';
import { AuthController } from './auth.controller';
import { AuthService } from './auth.service';
import { JwtModule } from '@nestjs/jwt';
import { env } from 'process';
import { UserService } from '../user/user.service';
import { BlockService } from '../block/block.service';
import { PasswordService } from '../password/password.service';
import { PrismaService } from 'src/infra/database/prisma.service';

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
    PrismaService
  ],
})
export class AuthModule {}
