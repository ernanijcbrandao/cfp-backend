package br.ejcb.cfp.seguranca.application.service.user;

import java.time.LocalDateTime;
import java.util.Optional;

import br.ejcb.cfp.seguranca.application.converter.UserConverter;
import br.ejcb.cfp.seguranca.application.dto.user.UpdateUserRequest;
import br.ejcb.cfp.seguranca.application.dto.user.UserResponse;
import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.application.message.UserMessage;
import br.ejcb.cfp.seguranca.application.usecase.user.IUpdateUserUseCase;
import br.ejcb.cfp.seguranca.application.validation.ConstraintBeanValidation;
import br.ejcb.cfp.seguranca.application.validation.UserValidation;
import br.ejcb.cfp.seguranca.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateUserUseCase implements IUpdateUserUseCase {

	private final UserService service;
	private final UserMessage messages;
	
	@Inject
	public UpdateUserUseCase(UserService service, UserMessage messages) {
		this.service = service;
		this.messages = messages;
	}
	
	@Transactional
	@Override
	public UserResponse update(Long userId, UpdateUserRequest request) throws ValidationException, UseCaseException {

		Optional<User> user = service.loadById(userId);
		UserValidation.validateUserNotFound(user, messages.userNotFound());
		UserValidation.validateUserInactive(user, messages.userInactive());

		User update = user.get();
		if (UserConverter.merge(update, request)) {

			ConstraintBeanValidation.validate(request);

			UserValidation.validatePreExistenceUser(service.loadByName(request.getName()),
					userId,
					messages.alreadyUserForThisName());

			UserValidation.validatePreExistenceUser(service.loadByEmail(request.getEmail()),
					userId,
					messages.alreadyUserForThisEmail());

			service.save(update.withLastUpdate(LocalDateTime.now()));
		}

		return UserConverter.toResponse(update);
	}

}
