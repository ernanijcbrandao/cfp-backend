package br.ejcb.cfp.seguranca.ms.domains.repository;

import java.util.List;

import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepositoryBase<Usuario, Long> {

	public Uni<List<Usuario>> buscarTodosUsuariosAtivo() {
		return list("WHERE ativo = true ORDER BY nome");
	}
	
}
