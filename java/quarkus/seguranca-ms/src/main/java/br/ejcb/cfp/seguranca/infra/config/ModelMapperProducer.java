package br.ejcb.cfp.seguranca.infra.config;

import org.modelmapper.ModelMapper;

import jakarta.enterprise.inject.Produces;

public class ModelMapperProducer {

	@Produces
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
