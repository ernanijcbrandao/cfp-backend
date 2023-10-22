import { Module } from '@nestjs/common';
import { PrismaService } from './infra/database/prisma.service';
import { UserModule } from './core/user/user.module';
import { UserController } from './core/user/user.controller';
import { DecimalUtilsService } from './util/decimal-utils-service';
import { UserService } from './core/user/user.service';

@Module({
  imports: [UserModule],
  controllers: [UserController],
  providers: [UserService, PrismaService, DecimalUtilsService],
})
export class AppModule {}
