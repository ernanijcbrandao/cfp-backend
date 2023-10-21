package br.ejcb.cfp.seguranca.ms.service;

import br.ejcb.cfp.seguranca.ms.domain.model.Senha;
import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import br.ejcb.cfp.seguranca.ms.domains.repository.SenhaRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SenhaService {

	@Inject
	SenhaRepository repository;

	public Uni<Senha> carregarSenhaAtiva(final Usuario usuario) {
		return repository.carregarSenhaAtiva(usuario);
	}

}
