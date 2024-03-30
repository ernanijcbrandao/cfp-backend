package br.ejcb.cfp.seguranca.application.service.user;

import java.time.LocalDateTime;
import java.util.UUID;

import br.ejcb.cfp.seguranca.application.converter.UserConverter;
import br.ejcb.cfp.seguranca.application.dto.user.CreateUserRequest;
import br.ejcb.cfp.seguranca.application.dto.user.UserResponse;
import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.application.message.UserMessage;
import br.ejcb.cfp.seguranca.application.usecase.user.ICreateUserUseCase;
import br.ejcb.cfp.seguranca.application.validation.ConstraintBeanValidation;
import br.ejcb.cfp.seguranca.application.validation.UserValidation;
import br.ejcb.cfp.seguranca.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUserUseCase implements ICreateUserUseCase {

	private final UserService service;
	private final UserMessage messages;
	
	@Inject
	public CreateUserUseCase(UserService service, UserMessage messages) {
		this.service = service;
		this.messages = messages;
	}
	
	@Override
	@Transactional
	public UserResponse create(CreateUserRequest request) throws ValidationException, UseCaseException {
		
		ConstraintBeanValidation.validate(request);

		UserValidation.validatePreExistenceUser(service.loadByLogin(request.getLogin()), 
				messages.alreadyUserForThisLogin());
		
		UserValidation.validatePreExistenceUser(service.loadByName(request.getName()), 
				messages.alreadyUserForThisName());
		
		UserValidation.validatePreExistenceUser(service.loadByEmail(request.getEmail()), 
				messages.alreadyUserForThisEmail());

		User user = UserConverter.toEntity(request)
				.withActive(Boolean.TRUE)
				.withCreated(LocalDateTime.now())
				.withPublicKey(UUID.randomUUID().toString());
		service.save(user);

		return UserConverter.toResponse(user);
	}

}
