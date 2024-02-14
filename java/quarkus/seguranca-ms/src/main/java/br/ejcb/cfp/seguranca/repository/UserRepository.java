package br.ejcb.cfp.seguranca.repository;

import java.util.Optional;

import br.ejcb.cfp.seguranca.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

	public Optional<User> loadByLogin(final String login) {
		return this.find("login", login)
				.firstResultOptional();
	}

	public long countByLogin(final String login) {
		return this.count("UPPER(login) = ?1", login.toUpperCase());
	}

	public long countByName(final String name) {
		return this.count("UPPER(name) = ?1", name.toUpperCase());
	}

	public long countByEmail(final String email) {
		return this.count("UPPER(email) = ?1", email.toUpperCase());
	}

}
