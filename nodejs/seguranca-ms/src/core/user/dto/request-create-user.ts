import { IsNotEmpty, IsNumber, IsOptional, Length, MaxLength } from "class-validator";
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class RequestCreateUser {
 
    @IsNotEmpty({
        message: 'o atributo \'name\' deve ser informado.'
    })
    @Length(5, 20, {
        message: 'O atributo \'name\' deve possuir no mínimo 5 e no máximo 20 caracateres.'
    })
    @ApiProperty()
    name: string;

    @ApiPropertyOptional()
    @IsOptional()
    @MaxLength(300, {
        message: 'O atributo \'description\' deve possuir no máximo 300 caracateres.'
    })
    description?: string;

    @IsNotEmpty({
        message: 'O atributo \'owner\' deve ser informado.'
    })
    @ApiProperty()
    owner: string;

    @IsNumber({}, {
        message: 'O atribtuo \'openingBalance\' deve ser informado e possuir um valor numérico válido'
    })
    @ApiProperty()
    openingBalance: number;

}