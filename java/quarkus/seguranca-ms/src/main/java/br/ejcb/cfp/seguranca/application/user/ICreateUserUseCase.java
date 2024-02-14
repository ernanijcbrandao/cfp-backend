package br.ejcb.cfp.seguranca.application.user;

import br.ejcb.cfp.seguranca.api.dto.CreateUserRequest;
import br.ejcb.cfp.seguranca.api.dto.UserResponse;
import br.ejcb.cfp.seguranca.resource.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.resource.exceptions.ValidationException;

public interface ICreateUserUseCase {
	
	/**
	 * criar um novo usuario na base de dados
	 * - validar se todos os dados da requisicao foram informados
	 * - validar se login informado ja existe para outro usuario
	 * - validar se name informado ja existe para outro usuario
	 * - validar se email ifnormado ja existe para outro usuario
	 * @param request
	 * @return usuario criado
	 * @throws ValidationException
	 * @throws UseCaseException
	 */
	UserResponse create (CreateUserRequest request)throws ValidationException, UseCaseException;

}
