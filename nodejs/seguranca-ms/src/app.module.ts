import { Module } from '@nestjs/common';
import { PrismaService } from './infra/database/prisma.service';
import { UserModule } from './core/user/user.module';
import { UserController } from './core/user/user.controller';
import { DecimalUtilsService } from './util/decimal-utils-service';
import { UserService } from './core/user/user.service';
import { AuthModule } from './core/auth/auth.module';
import { JwtStrategyService } from './core/jwt-strategy/jwt-strategy.service';
import { PasswordService } from './core/user/password.service';
import { BlockService } from './core/user/block.service';

@Module({
  imports: [UserModule, AuthModule],
  controllers: [UserController],
  providers: [
    UserService,
    PrismaService,
    DecimalUtilsService,
    JwtStrategyService,
    PasswordService,
    BlockService,
  ],
})
export class AppModule {}
