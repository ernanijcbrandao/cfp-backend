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

  @IsEnum(UserProfile, {
    message: "O atributo 'profile' deve ser informado com um dos seguintes valores [" + Object.values(UserProfile) + "]",
  })
  @ApiPropertyOptional()
  profile?: UserProfile;

}
