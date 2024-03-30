package br.ejcb.cfp.seguranca.application.service.user;

import java.util.List;
import java.util.Optional;

import br.ejcb.cfp.seguranca.domain.entity.Password;
import br.ejcb.cfp.seguranca.domain.entity.User;
import br.ejcb.cfp.seguranca.infra.repository.PasswordRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PasswordService {

	private final PasswordRepository repository;
	
	@Inject
	public PasswordService(PasswordRepository repository) {
		this.repository = repository;
	}
	
	public void save(Password password) {
		if (password.getId() != null) {
			password = repository.getEntityManager().merge(password);
		}
		repository.persistAndFlush(password);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public Optional<Password> loadActivePassword(final User user) {
		return repository.loadActivePassword(user);
	}

	public List<Password> listPasswords(final User user) {
		return repository.listPasswords(user);
	}
	
}
