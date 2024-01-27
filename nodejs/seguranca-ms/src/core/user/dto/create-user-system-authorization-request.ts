import { ApiProperty } from '@nestjs/swagger';
import {
  IsNotEmpty,
  MaxLength
} from 'class-validator';

export class CreateUserSystemAuthorizationRequest {

  @IsNotEmpty({
    message: "o atributo 'userId' deve ser informado.",
  })
  @ApiProperty()
  userId: string;

  @IsNotEmpty({
    message: "o atributo 'code' deve ser informado.",
  })
  @ApiProperty()
  code: string;

  @IsNotEmpty({
    message: "o atributo 'name' deve ser informado.",
  })
  @MaxLength(50, {
    message:
      "O atributo 'name' deve possuir no máximo 50 caracateres.",
  })
  @ApiProperty()
  name: string;

}
