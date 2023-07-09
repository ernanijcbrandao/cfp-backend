package br.ejcb.cfp.seguranca.ms.domains.repository;

import java.util.List;

import br.ejcb.cfp.seguranca.ms.domain.model.Bloqueio;
import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@WithSession
public class BloqueioRepository implements PanacheRepository<Bloqueio> {

	public Uni<List<Bloqueio>> listarBloqueiosAtivos(final Usuario usuario) {
		return find("WHERE usuario = :usuario AND ativo = true", 
				Parameters.with("usuario", usuario))
				.list();
	}

	public Uni<Long> countBloqueiosAtivos(final Usuario usuario) {
		return find("WHERE usuario = :usuario AND ativo = true", 
				Parameters.with("usuario", usuario)).count();
	}

}
