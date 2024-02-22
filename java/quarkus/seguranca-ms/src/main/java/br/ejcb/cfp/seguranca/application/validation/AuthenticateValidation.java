package br.ejcb.cfp.seguranca.application.validation;

import java.time.LocalDateTime;

import br.ejcb.cfp.seguranca.application.dto.AuthenticateRequest;
import br.ejcb.cfp.seguranca.application.exceptions.AuthenticateException;
import br.ejcb.cfp.seguranca.application.exceptions.BlockException;
import br.ejcb.cfp.seguranca.application.exceptions.PasswordException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.common.util.PasswordHashingUtils;
import br.ejcb.cfp.seguranca.domain.entity.Block;
import br.ejcb.cfp.seguranca.domain.entity.Password;
import br.ejcb.cfp.seguranca.domain.entity.User;

public class AuthenticateValidation {
	
	/**
	 * validar dados obrigatorios da requisicao
	 * @param request
	 * @throws ValidationException
	 */
	public static void validate(AuthenticateRequest request) throws ValidationException {
		requestValidate(request);
		usernameValidate(request);
		passwordValidate(request);
	}

	private static void requestValidate(AuthenticateRequest request) throws ValidationException {
		if (request == null) {
			throw new ValidationException("'request' não informado!");
		}
	}
	
	private static void usernameValidate(AuthenticateRequest request) throws ValidationException {
		if (request.getUsername() == null || request.getUsername().trim().length() == 0) {
			throw new ValidationException("'username' não informado!");
		}
	}

	private static void passwordValidate(AuthenticateRequest request) throws ValidationException {
		if (request.getPassword() == null || request.getPassword().trim().length() == 0) {
			throw new ValidationException("'password' não informado!");
		}
	}

	/**
	 * validar existencia e atividade do usuario a partir do login informado
	 * @param user
	 * @throws AuthenticateException
	 */
	public static void validate(final User user) throws AuthenticateException {
		if (user == null) {
			throw new AuthenticateException("'usuário' inválido!");
		}
		if (!Boolean.TRUE.equals(user.getActive())) {
			throw new AuthenticateException("'usuário' inativo!");
		}
	}

	/**
	 * validar senha cadastrada e senha informada na autenticacao
	 * - validar se existe senha ativa cadastrada para usuario
	 * - validar se senha cadastrada e ativa eh igual a informada na requisicao
	 * - validar se senha cadastrada e ativa esta expirada
	 * @param password
	 * @param request
	 * @throws PasswordException 
	 */
	public static void validate(final Password password, AuthenticateRequest request) throws PasswordException {
		validatePassword(password);
		verifyPassword(password, request);
		validateActivePasswordExpired(password);
	}

	private static void validateActivePasswordExpired(final Password password) throws PasswordException {
		boolean expired = password.getExpire() != null
				&& password.getExpire().isBefore(LocalDateTime.now());
		if (expired) {
			throw new PasswordException("Data expirada!");
		}
	}

	private static void verifyPassword(final Password password, AuthenticateRequest request) throws PasswordException {
		if (!PasswordHashingUtils.verifyPassword(request.getPassword(), password.getPassword())) {
			throw new PasswordException("Senha inválida");
		}
	}

	private static void validatePassword(final Password password) throws PasswordException {
		if (password == null) {
			throw new PasswordException("Não há senha cadastrada para este usuário");
		}
	}


	/**
	 * Validar a existencia de bloqueio ativo para o usuario
	 * @param block
	 * @throws BlockException 
	 */
	public static void validate(Block block) throws BlockException {
		if (block != null) {
			throw new BlockException(block.getReason());
		}
	}
	
}
