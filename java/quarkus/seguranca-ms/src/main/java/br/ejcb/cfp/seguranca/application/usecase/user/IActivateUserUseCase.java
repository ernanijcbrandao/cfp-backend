package br.ejcb.cfp.seguranca.application.usecase.user;

import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;

public interface IActivateUserUseCase {
	
	/**
	 * ativar o cadastro de um usuario
	 * - preciso validar se ID informado refere-se a um usuario que exista na base
	 * - preciso que o usuario informado esteja inativo
	 * @param request
	 */
	void activate (Long userId) throws UseCaseException;

}
