package br.ejcb.cfp.seguranca.application.user;

import br.ejcb.cfp.seguranca.api.dto.ChangePasswordRequest;

public interface ChangePasswordUseCase {

	void changePassword (Long userId, ChangePasswordRequest request);

}
