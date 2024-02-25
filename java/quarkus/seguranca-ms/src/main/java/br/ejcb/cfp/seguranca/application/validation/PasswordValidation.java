package br.ejcb.cfp.seguranca.application.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ejcb.cfp.seguranca.application.dto.ChangePasswordRequest;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;

public class PasswordValidation {

	/**
	 * validar preenchimento informacoes necessarias para troca de senha
	 * @param request senha atual e senha candidata a troca
	 * @throws ValidationException
	 */
	public static void validate(final ChangePasswordRequest request, final String message) throws ValidationException {
		boolean valid = request != null
				&& request.getPassword() != null && !request.getPassword().isEmpty()
				&& request.getNewPassword() != null && !request.getNewPassword().isEmpty()
				&& minimumRequirements(request);
		if (!valid) {
			throw new ValidationException(message);
		}
	}
	
	/**
	 * valida requisitos minimos para uma senha
	 * - conter no minimo uma letra minuscula
	 * - conter no minimo uma letra maiuscula
	 * - conter no minimo um digito
	 * - conter no minimo um caracter especial '!@#$%¨&_-=+'
	 * - possuir um tamanho minimo de 8 e maximo de 50 caracteres
	 * @param request
	 * @return resultado de requisitos alcansados ou nao
	 */
	private static boolean minimumRequirements(ChangePasswordRequest request) {
		String regExpn = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+-_=])(?=\\S+$).{8,50}$";

	    Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(request.getNewPassword());
	    
	    return matcher.matches();
	}

}
