package br.ejcb.cfp.seguranca.application.usecase.user;

import br.ejcb.cfp.seguranca.application.dto.user.CreateUserRequest;
import br.ejcb.cfp.seguranca.application.dto.user.UserResponse;
import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;

public interface ICreateUserUseCase {
	
	/**
	 * criar um novo usuario na base de dados
	 * - validar se todos os dados da requisicao foram informados
	 * - validar preenchimento de acordo com caracteristicas por tipo de informacao
	 * - validar se login informado ja existe para outro usuario
	 * - validar se name informado ja existe para outro usuario
	 * - validar se email ifnormado ja existe para outro usuario
	 * @param request
	 * @return usuario criado
	 * @throws ValidationExceptio
	 * @throws UseCaseException
	 */
	UserResponse create (CreateUserRequest request) throws ValidationException, UseCaseException;

}
