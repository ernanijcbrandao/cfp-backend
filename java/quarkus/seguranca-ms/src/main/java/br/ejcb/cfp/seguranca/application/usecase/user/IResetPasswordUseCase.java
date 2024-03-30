package br.ejcb.cfp.seguranca.application.usecase.user;

import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;

public interface IResetPasswordUseCase {

	/**
	 * realizar o reset de senha de usuario
	 * - usuario logado (token) devera possuir profile ADMIN ou ROOT
	 * - validar se usuario informado existe e esta ativo
	 * - alterar senha atual do usuario informado para FALSE
	 * @param userId id do usuario
	 */
	void resetPassword (Long userId) throws ValidationException, UseCaseException;

}
