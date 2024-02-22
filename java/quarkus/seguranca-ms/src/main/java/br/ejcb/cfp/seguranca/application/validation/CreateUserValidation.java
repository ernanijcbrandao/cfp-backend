package br.ejcb.cfp.seguranca.application.validation;

import java.util.Optional;

import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.domain.entity.User;

public class CreateUserValidation {
	
	/**
	 * validar pre existencia de usuario com mesmo login
	 * @param request
	 * @throws ValidationException
	 */
	public static void validatePreExistenceUser(final Optional<User> user, final String message) throws UseCaseException {
		if (user != null && user.isPresent()) {
			throw new UseCaseException(message);
		}
	}
	
}
