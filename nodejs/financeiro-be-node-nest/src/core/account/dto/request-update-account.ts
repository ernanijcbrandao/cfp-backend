import { IsNotEmpty, IsNumber, IsOptional, Length, MaxLength } from "class-validator";
import { ApiPropertyOptional } from '@nestjs/swagger';

export class RequestUpdateAccount {
 
    @IsOptional()
    @Length(5, 20, {
        message: 'O atributo \'name\' deve possuir no mínimo 5 e no máximo 20 caracateres.'
    })
    @ApiPropertyOptional()
    name: string;

    @ApiPropertyOptional()
    @IsOptional()
    @MaxLength(300, {
        message: 'O atributo \'description\' deve possuir no máximo 300 caracateres.'
    })
    description?: string;

    @IsOptional()
    @ApiPropertyOptional()
    owner: string;

    @IsOptional()
    @IsNumber({}, {
        message: 'O atribtuo \'openingBalance\' deve ser informado e possuir um valor numérico válido'
    })
    @ApiPropertyOptional()
    openingBalance: number;

}