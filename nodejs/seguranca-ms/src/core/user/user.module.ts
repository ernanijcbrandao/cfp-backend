import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { PrismaService } from 'src/infra/database/prisma.service';
import { DecimalUtilsService } from 'src/util/decimal-utils-service';
import { BlockService } from './block.service';
import { PasswordService } from './password.service';
import { SystemsService } from './systems.service';
import { UserController } from './user.controller';
import { UserService } from './user.service';

@Module({
  imports: [
    JwtModule.register({
      secret: process.env.SECRET_PHRASE,
      signOptions: {
        expiresIn: process.env.TIMEOUT_TOKEN,
      },
    }),
  ],
  controllers: [UserController],
  providers: [UserService, 
    PrismaService, 
    DecimalUtilsService, 
    PasswordService, 
    BlockService, 
    SystemsService
    // JwtStrategyService
  ],
})

export class UserModule {
}
