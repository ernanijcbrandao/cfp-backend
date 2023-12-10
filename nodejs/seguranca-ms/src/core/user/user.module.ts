import { Module } from '@nestjs/common';
import { UserService } from './user.service';
import { PrismaService } from 'src/infra/database/prisma.service';
import { UserController } from './user.controller';
import { DecimalUtilsService } from 'src/util/decimal-utils-service';
import { PasswordService } from './password.service';
import { BlockService } from './block.service';

@Module({
  imports: [],
  controllers: [UserController],
  providers: [UserService, PrismaService, DecimalUtilsService, PasswordService, BlockService],
})
export class UserModule {}
