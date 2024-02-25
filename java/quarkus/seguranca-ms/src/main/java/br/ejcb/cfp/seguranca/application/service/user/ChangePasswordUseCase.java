package br.ejcb.cfp.seguranca.application.service.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.ejcb.cfp.seguranca.application.dto.ChangePasswordRequest;
import br.ejcb.cfp.seguranca.application.exceptions.PasswordException;
import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.application.message.UserMessage;
import br.ejcb.cfp.seguranca.application.usecase.user.IChangePasswordUseCase;
import br.ejcb.cfp.seguranca.application.validation.ConstraintBeanValidation;
import br.ejcb.cfp.seguranca.application.validation.PasswordValidation;
import br.ejcb.cfp.seguranca.application.validation.UserValidation;
import br.ejcb.cfp.seguranca.common.util.PasswordHashingUtils;
import br.ejcb.cfp.seguranca.domain.entity.Password;
import br.ejcb.cfp.seguranca.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ChangePasswordUseCase implements IChangePasswordUseCase {

	private final UserService userService;
	private final PasswordService passwordService;
	private final UserMessage messages;
	
	@Inject
	public ChangePasswordUseCase(UserService userService, 
			PasswordService passwordService,
			UserMessage messages) {
		this.userService = userService;
		this.passwordService = passwordService;
		this.messages = messages;
	}

	@Transactional
	@Override
	public void changePassword(Long userId, ChangePasswordRequest request) throws ValidationException, UseCaseException, PasswordException {
	
		final Optional<User> user = userService.loadById(userId);
		requestValidate(user, request);
		validateCurrentPassword(user.get(), request);
		
		final List<Password> passwords = passwordService.listPasswords(user.get());		
		validateNewPasswordInHistory(passwords, request);
		
		saveNewPassword(user.get(), request);
		
		updatePasswordHistory(passwords);
		
	}
	
	private void requestValidate(final Optional<User> user, 
			final ChangePasswordRequest request) throws ValidationException, UseCaseException {
		UserValidation.validateUserNotFound(user,messages.userNotFound());
		UserValidation.validateUserInactive(user,messages.alreadyUserForThisLogin());
		
		ConstraintBeanValidation.validate(request);
		
		PasswordValidation.validate(request, messages.requestChangePasswordInsufficient());
	}
	
	/**
	 * validar senha corrente
	 * - caso o usuario ainda nao possua uma senha cadastrada, usar o e-mail de cadastro como
	 *   senha para validacao
	 * - do contrario comparar senha informada na requisicao com a senha ativa cadastrada
	 * , em caso de comparacao invalida lancar uma excecao
	 * @param user
	 * @param request
	 * @throws PasswordException 
	 */
	private void validateCurrentPassword(final User user, final ChangePasswordRequest request) throws PasswordException {
		Optional<Password> password = passwordService.loadActivePassword(user);
		boolean valid = false;
		if (password.isEmpty()) {
			valid = user.getEmail().equals(request.getPassword());
		} else {
			valid = PasswordHashingUtils.verifyPassword(request.getPassword(), password.get().getPassword());
		}
		if (!valid) {
			throw new PasswordException(messages.passwordInvalid());
		}
	}
	
	/**
	 * procurar no historico de senhas, se nova senha ja foi utilizada
	 * - objetivo impedir reutilizacao de senha conforme N ultimas senhas cadastradas
	 * @param passwords
	 * @param request
	 * @throws PasswordException
	 */
	private void validateNewPasswordInHistory(final List<Password> passwords, final ChangePasswordRequest request) throws PasswordException {
		if (passwords != null) {
			for (Password password : passwords) {
				if (PasswordHashingUtils.verifyPassword(request.getNewPassword(), password.getPassword()) ) {
					throw new PasswordException(messages.newPasswordUsedLastUpdates());
				}
			}
		}
	}
	
	private void saveNewPassword(final User user, final ChangePasswordRequest request) {
		// TODO buscar parametro tempo para expiracao de senha
		LocalDateTime expire = LocalDateTime.now().plusDays(70);
		
		passwordService.save(Password.create()
				.withActive(Boolean.TRUE)
				.withExpire(expire)
				.withCreated(LocalDateTime.now())
				.withInvalidAttempt(0)
				.withUser(user)
				.withPassword(PasswordHashingUtils.hashPassword(request.getNewPassword()))
				);
	}
	
	/**
	 * atualizar o historico de ultimas senhas alteradas conforme parametrizacao
	 * @param passwords
	 */
	private void updatePasswordHistory(List<Password> passwords) {
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
