package br.ejcb.cfp.seguranca.ms.domains.repository;

import java.time.LocalDateTime;

import br.ejcb.cfp.seguranca.ms.domain.model.Bloqueio;
import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@WithSession
public class BloqueioRepository implements PanacheRepository<Bloqueio> {

	public Uni<Bloqueio> buscarBloqueioAtivo(final Usuario usuario) {
		return find("WHERE usuario = :usuario AND ativo = true", 
				Parameters.with("usuario", usuario)).firstResult();
	}

	public Uni<Long> countBloqueiosAtivos(final Usuario usuario) {
		return find("WHERE usuario = :usuario AND ativo = true", 
				Parameters.with("usuario", usuario)).count();
	}

	@Transactional
	public Uni<Integer> limparBloqueiosTemporariosExpiradosEAtivos(final Usuario usuario) {
		return update("ativo = false WHERE usuario = :usuario AND ativo = true AND dataHoraExpiracaoBloqueio < :datahora ", 
				Parameters.with("usuario", usuario).and("datahora", LocalDateTime.now()));
	}

}
