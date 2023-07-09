package br.ejcb.cfp.seguranca.ms.domains.repository;

import br.ejcb.cfp.seguranca.ms.domain.model.Senha;
import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@WithSession
public class SenhaRepository implements PanacheRepository<Senha> {

	public Uni<Senha> carregarSenhaAtiva(final Usuario usuario) {
		return find("WHERE usuario = :usuario AND ativo = true", 
				Parameters.with("usuario", usuario))
				.firstResult();
	}

}
