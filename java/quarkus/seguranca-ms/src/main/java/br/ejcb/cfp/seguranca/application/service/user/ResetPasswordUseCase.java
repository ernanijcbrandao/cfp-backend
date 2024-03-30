package br.ejcb.cfp.seguranca.application.service.user;

import java.util.List;
import java.util.Optional;

import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.application.message.UserMessage;
import br.ejcb.cfp.seguranca.application.usecase.user.IResetPasswordUseCase;
import br.ejcb.cfp.seguranca.application.validation.UserValidation;
import br.ejcb.cfp.seguranca.domain.entity.Password;
import br.ejcb.cfp.seguranca.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ResetPasswordUseCase implements IResetPasswordUseCase {

	private final UserService userService;
	private final PasswordService passwordService;
	private final UserMessage messages;
	
	@Inject
	public ResetPasswordUseCase(UserService userService, 
			PasswordService passwordService,
			UserMessage messages) {
		this.userService = userService;
		this.passwordService = passwordService;
		this.messages = messages;
	}

	@Transactional
	@Override
	public void resetPassword(Long userId) throws ValidationException, UseCaseException {

		// TODO validar, o profile do usuario logado, somente ADMIN ou ROOT podera executar este metodo
		final Optional<User> user = userService.loadById(userId);
		UserValidation.validateUserNotFound(user,messages.userNotFound());
		UserValidation.validateUserInactive(user,messages.alreadyUserForThisLogin());
		
		final List<Password> passwords = passwordService.listPasswords(user.get());		
		
		resetPasswordAndAdjustHistory(passwords);
		
	}
	
	/**
	 * atualizar historico de ultimas senhas alteradas conforme parametrizacao,
	 * inclusive passando a atual senha ativa para false
	 * @param passwords
	 */
	private void resetPasswordAndAdjustHistory(List<Password> passwords) {
		if (passwords == null) {
			return;
		}
		
		// TODO recuperar quantidade de senhas em historico
		int history = 5;
		
		int posicao = 0;
		for (Password password : passwords) {
			if (++posicao < (history -1)) {
				passwordService.save(password.withActive(Boolean.FALSE));
			} else {
				passwordService.delete(password.getId());
			}
		}
		
	}

}
