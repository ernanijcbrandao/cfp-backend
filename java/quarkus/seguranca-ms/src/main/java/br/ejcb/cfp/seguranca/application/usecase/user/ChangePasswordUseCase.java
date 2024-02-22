package br.ejcb.cfp.seguranca.application.usecase.user;

import br.ejcb.cfp.seguranca.application.dto.ChangePasswordRequest;

public interface ChangePasswordUseCase {

	void changePassword (Long userId, ChangePasswordRequest request);

}
