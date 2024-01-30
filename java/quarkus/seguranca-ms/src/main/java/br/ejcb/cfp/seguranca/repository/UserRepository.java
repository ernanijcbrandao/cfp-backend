package br.ejcb.cfp.seguranca.repository;

import java.util.Optional;

import br.ejcb.cfp.seguranca.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

	public Optional<User> loadByLogin(final String login) {
		return this.find("login", login)
				.firstResultOptional();
	}

}
