package br.ejcb.cfp.seguranca.repository;

import java.util.List;
import java.util.Optional;

import br.ejcb.cfp.seguranca.model.Block;
import br.ejcb.cfp.seguranca.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlockRepository implements PanacheRepository<Block> {

	public List<Block> listActiveLocks(final User user) {
		return this.list("user = ?1 and active = true", user);
	}

	public Optional<Block> loadFirstActiveLocks(final User user) {
		return this.find("user = ?1 and active = true", user)
				.firstResultOptional();
	}

}
