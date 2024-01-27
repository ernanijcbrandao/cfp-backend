import { MiddlewareConsumer, Module, NestModule } from '@nestjs/common';
import { AuthModule } from './core/auth/auth.module';
import { JwtStrategyService } from './core/jwt-strategy/jwt-strategy.service';
import { CorsMiddleware } from './core/middleware/cors-middleware';
import { BlockService } from './core/user/block.service';
import { PasswordService } from './core/user/password.service';
import { UserController } from './core/user/user.controller';
import { UserModule } from './core/user/user.module';
import { UserService } from './core/user/user.service';
import { PrismaService } from './infra/database/prisma.service';
import { DecimalUtilsService } from './util/decimal-utils-service';

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
export class AppModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    consumer.apply(CorsMiddleware).forRoutes('*');
  }
}
