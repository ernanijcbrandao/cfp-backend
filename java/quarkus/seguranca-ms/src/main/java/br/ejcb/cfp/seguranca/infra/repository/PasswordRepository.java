package br.ejcb.cfp.seguranca.infra.repository;

import java.util.List;
import java.util.Optional;

import br.ejcb.cfp.seguranca.domain.entity.Password;
import br.ejcb.cfp.seguranca.domain.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordRepository implements PanacheRepository<Password> {

	public Optional<Password> loadActivePassword(final User user) {
		return this.find("user = ?1 and active = true", user)
				.firstResultOptional();
	}

	public List<Password> listPasswords(final User user) {
		return this.list("user = ?1 order by created desc", user);
	}

}
