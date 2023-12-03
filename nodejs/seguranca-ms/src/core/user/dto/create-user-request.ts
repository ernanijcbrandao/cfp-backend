import {
  IsEmail,
  IsEnum,
  IsNotEmpty,
  Length,
  MaxLength,
} from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';
import { UserProfile } from './user-profile.enum';

export class CreateUserRequest {
  @IsNotEmpty({
    message: "o atributo 'name' deve ser informado.",
  })
  @Length(5, 50, {
    message:
      "O atributo 'name' deve possuir no mínimo 5 e no máximo 50 caracateres.",
  })
  @ApiProperty()
  name: string;

  @IsNotEmpty({
    message: "o atributo 'email' deve ser informado.",
  })
  @MaxLength(100, {
    message: "O atributo 'email' deve possuir no máximo 100 caracateres.",
  })
  @IsEmail()
  @ApiProperty()
  email: string;

  @IsNotEmpty({
    message: "o atributo 'login' deve ser informado.",
  })
  @Length(5, 30, {
    message:
      "O atributo 'login' deve possuir no mínimo 5 e no máximo 30 caracateres.",
  })
  @ApiProperty()
  login: string;

  @IsEnum(UserProfile, {
    message: "O atributo 'profile' deve ser informado com um dos seguintes valores [" + Object.values(UserProfile) + "]",
  })
  @ApiProperty()
  profile: UserProfile;
}
