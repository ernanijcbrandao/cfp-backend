import { IsEmail, IsEnum, Length, MaxLength } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';
import { UserProfile } from './user-profile.enum';

export class RequestUpdateUser {
  @Length(5, 50, {
    message:
      "O atributo 'name' deve possuir no mínimo 5 e no máximo 50 caracateres.",
  })
  @ApiPropertyOptional()
  name?: string;

  @MaxLength(100, {
    message: "O atributo 'email' deve possuir no máximo 100 caracateres.",
  })
  @IsEmail()
  @ApiPropertyOptional()
  email?: string;

  @IsEnum(UserProfile)
  @ApiPropertyOptional()
  profile?: UserProfile;
}
