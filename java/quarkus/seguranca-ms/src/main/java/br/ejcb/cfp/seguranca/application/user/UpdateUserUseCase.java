package br.ejcb.cfp.seguranca.application.user;

import br.ejcb.cfp.seguranca.api.dto.UpdateUserRequest;
import br.ejcb.cfp.seguranca.api.dto.UserResponse;

public interface UpdateUserUseCase {
	
	UserResponse update (UpdateUserRequest request);

}
