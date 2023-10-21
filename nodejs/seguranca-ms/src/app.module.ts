import { Module } from '@nestjs/common';
import { PrismaService } from './infra/database/prisma.service';
import { AccountModule } from './core/account/account.module';
import { AccountController } from './core/account/account.controller';
import { DecimalUtilsService } from './util/decimal-utils-service';
import { AccountService } from './core/account/account.service';

@Module({
  imports: [AccountModule],
  controllers: [AccountController],
  providers: [AccountService, PrismaService, DecimalUtilsService],
})
export class AppModule {}
