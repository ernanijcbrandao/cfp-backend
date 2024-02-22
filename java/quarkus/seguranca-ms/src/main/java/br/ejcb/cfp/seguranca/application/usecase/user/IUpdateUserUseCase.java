package br.ejcb.cfp.seguranca.application.usecase.user;

import br.ejcb.cfp.seguranca.application.dto.UpdateUserRequest;
import br.ejcb.cfp.seguranca.application.dto.UserResponse;

public interface IUpdateUserUseCase {
	
	UserResponse update (UpdateUserRequest request);

}
