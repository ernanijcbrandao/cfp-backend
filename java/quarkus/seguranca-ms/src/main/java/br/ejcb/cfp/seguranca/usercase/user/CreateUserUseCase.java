package br.ejcb.cfp.seguranca.usercase.user;

import java.time.LocalDateTime;
import java.util.UUID;

import br.ejcb.cfp.seguranca.api.dto.CreateUserRequest;
import br.ejcb.cfp.seguranca.api.dto.UserResponse;
import br.ejcb.cfp.seguranca.application.user.ICreateUserUseCase;
import br.ejcb.cfp.seguranca.domain.User;
import br.ejcb.cfp.seguranca.repository.UserRepository;
import br.ejcb.cfp.seguranca.resource.converter.UserConverter;
import br.ejcb.cfp.seguranca.resource.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.resource.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.resource.validation.CreateUserValidation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUserUseCase implements ICreateUserUseCase {

	private final UserRepository repository;
	
	@Inject
	public CreateUserUseCase(UserRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public UserResponse create(CreateUserRequest request) throws ValidationException, UseCaseException {
		CreateUserValidation.validate(request);
		verifyIfLoginInUse(request.getLogin());
		verifyIfNameInUse(request.getLogin());
		verifyIfEmailInUse(request.getEmail());
		
		User user = UserConverter.toEntity(request)
				.withActive(Boolean.TRUE)
				.withCreated(LocalDateTime.now())
				.withPublicKey(UUID.randomUUID().toString());
		repository.persistAndFlush(user);
		
		return UserConverter.toResponse(user);
	}
	
	private void verifyIfLoginInUse(String login) throws UseCaseException {
		long test = repository.countByLogin(login);
		if (test > 0) {
			throw new UseCaseException("O 'login' informado já está em uso");
		}
	}
	
	private void verifyIfNameInUse(String name) throws UseCaseException {
		long test = repository.countByName(name);
		if (test > 0) {
			throw new UseCaseException("O 'name' informado já está em uso");
		}
	}

	private void verifyIfEmailInUse(String email) throws UseCaseException {
		long test = repository.countByEmail(email);
		if (test > 0) {
			throw new UseCaseException("O 'email' informado já está em uso");
		}
	}


}
