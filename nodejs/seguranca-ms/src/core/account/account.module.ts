import { Module } from '@nestjs/common';
import { AccountService } from './account.service';
import { PrismaService } from 'src/infra/database/prisma.service';
import { AccountController } from './account.controller';
import { DecimalUtilsService } from 'src/util/decimal-utils-service';

@Module({
  imports: [],
  controllers: [AccountController],
  providers: [AccountService, PrismaService, DecimalUtilsService],
})
export class AccountModule {}
