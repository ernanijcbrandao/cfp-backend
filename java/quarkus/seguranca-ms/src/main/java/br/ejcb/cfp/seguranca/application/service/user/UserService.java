package br.ejcb.cfp.seguranca.application.service.user;

import java.util.List;
import java.util.Optional;

import br.ejcb.cfp.seguranca.application.dto.user.SearchUsersFilter;
import br.ejcb.cfp.seguranca.domain.entity.User;
import br.ejcb.cfp.seguranca.infra.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

	private final UserRepository repository;
	
	@Inject
	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	
	public void save(User user) {
		if (user.getId() != null) {
			user = repository.getEntityManager().merge(user);
		}
		repository.persistAndFlush(user);
	}
	
	public Optional<User> loadById(final Long id) {
		return repository.findByIdOptional(id);
	}
	
	public Optional<User> loadByLogin(final String login) {
		return repository.loadByLogin(login);
	}
	
	public Optional<User> loadByName(final String name) {
		return repository.loadByName(name);
	}
	
	public Optional<User> loadByEmail(final String email) {
		return repository.loadByEmail(email);
	}
	
	public Optional<User> loadByPublicKey(final String publicKey) {
		return repository.loadByPublicKey(publicKey);
	}
	
	public List<User> search(final SearchUsersFilter filter) {
		return repository.search(filter);
	}

}
