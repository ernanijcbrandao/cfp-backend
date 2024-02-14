package br.ejcb.cfp.seguranca.application.user;

import br.ejcb.cfp.seguranca.api.dto.UserResponse;

public interface LoadUserCaseByIdUseCase {
	
	UserResponse load (Long userId);

}
