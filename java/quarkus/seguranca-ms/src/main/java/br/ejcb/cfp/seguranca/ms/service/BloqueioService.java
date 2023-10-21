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

	public Uni<Boolean> possuiBloqueioAtivo(final Usuario usuario) {
		return repository.countBloqueiosAtivos(usuario)
				.onItem().transformToUni(total -> {
					if (total == 0) {
						return Uni.createFrom().item(false);
					}
					return repository.limparBloqueiosTemporariosExpiradosEAtivos(usuario)
							.onItem().transformToUni(atualizado -> Uni.createFrom().item(total != Long.valueOf(atualizado)));
				});
	}

}
