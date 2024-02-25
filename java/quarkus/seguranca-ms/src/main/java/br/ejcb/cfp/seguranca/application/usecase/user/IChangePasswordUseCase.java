package br.ejcb.cfp.seguranca.application.usecase.user;

import br.ejcb.cfp.seguranca.application.dto.ChangePasswordRequest;
import br.ejcb.cfp.seguranca.application.exceptions.PasswordException;
import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;

public interface IChangePasswordUseCase {

	/**
	 * realizar alteracao de senha do usuario
	 * - validar se usuario informado existe e esta ativo
	 * - validar senha atual
	 * - validar requisitos minimos de composicao da nova senha: 
	 *   no minimo 1 letra maiuscula e 1 minuscula, 1 digito 
	 *   e um simbolo (!@#$%¨&()_+-=)
	 * - validar se nova senha ainda n foi utilizada nas ultimas N trocas
	 * - caso seja a primeira senha do usuario (n existe uma senha atual)
	 *   como senha atual utilizar o e-mail cadastrado - validar o mesmo
	 * @param userId id do usuario
	 * @param request senha atual e nova senha
	 */
	void changePassword (Long userId, ChangePasswordRequest request) throws ValidationException, UseCaseException, PasswordException;

}
