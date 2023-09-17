package br.ejcb.lotto.command.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import picocli.CommandLine.ITypeConverter;

public class LocalDateConverter implements ITypeConverter<LocalDate>{

	@Override
	public LocalDate convert(String value) throws Exception {
		return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

}
