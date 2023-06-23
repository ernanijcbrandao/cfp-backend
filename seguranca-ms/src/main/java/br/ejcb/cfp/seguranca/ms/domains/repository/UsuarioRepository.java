package br.ejcb.cfp.seguranca.ms.domains.repository;

import java.util.List;

import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@WithSession
public class UsuarioRepository implements PanacheRepositoryBase<Usuario, Long> {

	public Uni<List<Usuario>> listarSomenteAtivos() {
		return list("WHERE ativo = true ORDER BY nome");
	}

	public Uni<Usuario> carregar(final String login) {
		return find("login", login).firstResult();
	}
	
}
