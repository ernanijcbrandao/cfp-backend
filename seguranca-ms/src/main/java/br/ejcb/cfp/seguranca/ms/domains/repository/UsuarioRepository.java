package br.ejcb.cfp.seguranca.ms.domains.repository;

import java.util.List;

import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

	
	public Uni<List<Usuario>> buscarTodosUsuariosAtivo() {
		return list("where ativo = true order by nome");
	}
	
}
