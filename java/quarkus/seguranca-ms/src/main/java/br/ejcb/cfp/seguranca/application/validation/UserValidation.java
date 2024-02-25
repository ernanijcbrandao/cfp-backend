package br.ejcb.cfp.seguranca.application.validation;

import java.util.Optional;

import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.domain.entity.User;

public class UserValidation {
	
	/**
	 * validar existencia de usuario
	 * @param user
	 * @throws UseCaseException
	 */
	public static void validateUserNotFound(final Optional<User> user, final String message) throws UseCaseException {
		if (user == null || user.isEmpty()) {
			throw new UseCaseException(message);
		}
	}
	
	/**
	 * validar usuario inativo
	 * @param user
	 * @throws UseCaseException
	 */
	public static void validateUserInactive(final Optional<User> user, final String message) throws UseCaseException {
		if (!Boolean.TRUE.equals(user.get().getActive())) {
			throw new UseCaseException(message);
		}
	}
	
	/**
	 * validar usuario ativo
	 * @param user
	 * @throws UseCaseException
	 */
	public static void validateUserActive(final Optional<User> user, final String message) throws UseCaseException {
		if (Boolean.TRUE.equals(user.get().getActive())) {
			throw new UseCaseException(message);
		}
	}
	
	/**
	 * validar pre existencia de usuario
	 * @param user
	 * @throws UseCaseException
	 */
	public static void validatePreExistenceUser(final Optional<User> user, final String message) throws UseCaseException {
		if (user != null && user.isPresent()) {
			throw new UseCaseException(message);
		}
	}
	
	/**
	 * validar pre existencia de usuario diferente
	 * @param user
	 * @throws UseCaseException
	 */
	public static void validatePreExistenceUser(final Optional<User> user, final Long userId, final String message) throws UseCaseException {
		if (user != null && user.isPresent() && !user.get().getId().equals(userId)) {
			throw new UseCaseException(message);
		}
	}
	
}
