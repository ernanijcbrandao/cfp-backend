package br.ejcb.cfp.seguranca.ms.service;

import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import br.ejcb.cfp.seguranca.ms.domains.repository.BloqueioRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BloqueioService {

	@Inject
	BloqueioRepository repository;

	public Uni<Boolean> tratarBloqueiosAtivos(final Usuario usuario) {
		return null;
//				Uni.createFrom().item(() -> {
//			
//		});
	}

}
