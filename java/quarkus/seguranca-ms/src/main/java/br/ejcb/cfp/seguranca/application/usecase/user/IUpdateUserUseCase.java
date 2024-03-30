package br.ejcb.cfp.seguranca.application.usecase.user;

import br.ejcb.cfp.seguranca.application.dto.user.UpdateUserRequest;
import br.ejcb.cfp.seguranca.application.dto.user.UserResponse;
import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;

public interface IUpdateUserUseCase {
	
	/**
	 * atualizar os dados de cadastrado de um determinado usuario ativo
	 * - preciso validar se ID informado refere-se a um usuario que exista na base
	 * - preciso que o usuario informado esteja ativo
	 * - somente poderei atualizar os seguintes dados: nome, email, profile
	 * - garantir que nome continue unico
	 * - garantir que email continue unico
	 * - profile seja valido
	 * @param request
	 * @return dados usuario atualizado
	 */
	UserResponse update (Long userId, UpdateUserRequest request) throws ValidationException, UseCaseException;

}
