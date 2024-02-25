package br.ejcb.cfp.seguranca.application.service.user;

import java.time.LocalDateTime;
import java.util.Optional;

import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.message.UserMessage;
import br.ejcb.cfp.seguranca.application.usecase.user.IActivateUserUseCase;
import br.ejcb.cfp.seguranca.application.validation.UserValidation;
import br.ejcb.cfp.seguranca.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ActivateUserUseCase implements IActivateUserUseCase {

	private final UserService service;
	private final UserMessage messages;
	
	@Inject
	public ActivateUserUseCase(UserService service, UserMessage messages) {
		this.service = service;
		this.messages = messages;
	}
	
	@Transactional
	@Override
	public void activate(Long userId) throws UseCaseException {
		Optional<User> user = service.loadById(userId);
		UserValidation.validateUserNotFound(user, messages.userNotFound());
		UserValidation.validateUserActive(user, messages.userActive());

		User update = user.get();
		service.save(update
				.withActive(Boolean.TRUE)
				.withLastUpdate(LocalDateTime.now()));
	}

}
