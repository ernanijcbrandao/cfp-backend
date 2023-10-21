package br.ejcb.lotto.command.converter;

import java.math.BigDecimal;

import picocli.CommandLine.ITypeConverter;

public class BigDecimalConverter implements ITypeConverter<BigDecimal>{

	@Override
	public BigDecimal convert(String value) throws Exception {
		return BigDecimal.valueOf(Double.valueOf(value));
	}

}
