package br.ejcb.cfp.seguranca.application.validation;

import br.ejcb.cfp.seguranca.application.dto.ChangePasswordRequest;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;

public class PasswordValidation {

	/**
	 * validar preenchimento informacoes necessarias para troca de senha
	 * valida requisitos minimos para a nova senha
	 * - conter no minimo uma letra minuscula
	 * - conter no minimo uma letra maiuscula
	 * - conter no minimo um digito
	 * - conter no minimo um caracter especial '!@#$%¨&_-=+'
	 * - possuir um tamanho minimo de 8 e maximo de 50 caracteres
	 * @param request senha atual e senha candidata a troca
	 * @throws ValidationException
	 */
	public static void validate(final ChangePasswordRequest request, final String message) throws ValidationException {
		boolean valid = request != null
				&& request.getPassword() != null && !request.getPassword().isEmpty()
				&& request.getNewPassword() != null && !request.getNewPassword().isEmpty()
				&& minimumSmallLetters(request)
				&& minimumCapitalLetters(request)
				&& minimumDigits(request)
				&& minimumSpecialCharacter(request)
				&& minAndMaxLength(request);
		if (!valid) {
			throw new ValidationException(message);
		}
	}
	
	private static boolean minimumSmallLetters(ChangePasswordRequest request) {
		boolean valid = false;
		for (int position=0; position<request.getNewPassword().length() && !valid; position++) {
			char charCompare = request.getNewPassword().charAt(position);
			valid = charCompare >= 'a' && charCompare <= 'z';
		}
		return valid;
	}
	
	private static boolean minimumCapitalLetters(ChangePasswordRequest request) {
		boolean valid = false;
		for (int position=0; position<request.getNewPassword().length() && !valid; position++) {
			char charCompare = request.getNewPassword().charAt(position);
			valid = charCompare >= 'A' && charCompare <= 'Z';
		}
		return valid;
	}

	private static boolean minimumDigits(ChangePasswordRequest request) {
		boolean valid = false;
		for (int position=0; position<request.getNewPassword().length() && !valid; position++) {
			char charCompare = request.getNewPassword().charAt(position);
			valid = charCompare >= '0' && charCompare <= '9';
		}
		return valid;
	}

	private static char[] charactersSpecial = new char[] {'!','@','#','$','%','^','~','&','(',')','_','+','=','-'};
	private static boolean minimumSpecialCharacter(ChangePasswordRequest request) {
		boolean valid = false;
		for (int position=0; position<request.getNewPassword().length() && !valid; position++) {
			char charCompare = request.getNewPassword().charAt(position);
			for (int elementPos = 0; elementPos < charactersSpecial.length && !valid; elementPos++) {
				valid = charCompare == charactersSpecial[elementPos];
			}
		}
		return valid;
	}

	private static boolean minAndMaxLength(ChangePasswordRequest request) {
		boolean valid = request.getNewPassword().length() >= 8 
				&& request.getNewPassword().length() <= 50;
		return valid;
	}

}
