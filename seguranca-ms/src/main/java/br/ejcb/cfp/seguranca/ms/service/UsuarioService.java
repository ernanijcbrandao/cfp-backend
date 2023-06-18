package br.ejcb.cfp.seguranca.ms.service;

import java.util.List;
import java.util.stream.Collectors;

import br.ejcb.cfp.seguranca.ms.converters.UsuarioConverter;
import br.ejcb.cfp.seguranca.ms.domains.repository.UsuarioRepository;
import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioDTO;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioService {

	@Inject
	UsuarioRepository repository;

	public Uni<List<UsuarioDTO>> listarUsuariosAtivos() {
		return repository.buscarTodosUsuariosAtivo()
				.map(entityList -> entityList.stream()
						.map(UsuarioConverter::toDTO)
						.collect(Collectors.toList()));
	}

}
