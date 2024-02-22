package br.ejcb.cfp.seguranca.application.usecase.user;

import java.util.List;

import br.ejcb.cfp.seguranca.application.dto.SearchUsersFilter;
import br.ejcb.cfp.seguranca.application.dto.UserResponse;
import br.ejcb.cfp.seguranca.application.exceptions.NotFoundException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;

public interface ISearchUsersUseCase {
	
	/**
	 * carregar informacoes de um determinado usuario a partir de seu publicKey
	 * @param publicKey
	 * @return
	 */
	List<UserResponse> search (SearchUsersFilter filter) throws ValidationException, NotFoundException;

}
