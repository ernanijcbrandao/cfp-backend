import { Injectable } from '@nestjs/common';

@Injectable()
export class DecimalUtilsService {

  // Método para converter uma string em decimal
  convertStringToDecimal(decimalString: string): number {
    const decimalNumber = parseFloat(decimalString);
    return decimalNumber;
  }

  // Método para converter um float em string com 2 casas decimais
  convertDecimalToStringWithTwoDecimals(value: number): string {
    // Arredonda o valor para 2 casas decimais e o converte para uma string
    return value.toFixed(2);
  }

}