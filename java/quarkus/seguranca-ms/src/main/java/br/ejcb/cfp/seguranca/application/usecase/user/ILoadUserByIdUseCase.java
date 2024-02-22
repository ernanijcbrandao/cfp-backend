package br.ejcb.cfp.seguranca.application.usecase.user;

import br.ejcb.cfp.seguranca.application.dto.UserResponse;

public interface ILoadUserByIdUseCase {
	
	/**
	 * carregar informacoes de um determinado usuario a partir de seu id
	 * @param userId
	 * @return
	 */
	UserResponse load (Long userId);

}
