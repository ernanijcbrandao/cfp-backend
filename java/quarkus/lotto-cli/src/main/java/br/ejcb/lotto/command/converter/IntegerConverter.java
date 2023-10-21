package br.ejcb.lotto.command.converter;

import picocli.CommandLine.ITypeConverter;

public class IntegerConverter implements ITypeConverter<Integer>{

	@Override
	public Integer convert(String value) throws Exception {
		return Integer.valueOf(value);
	}

}
