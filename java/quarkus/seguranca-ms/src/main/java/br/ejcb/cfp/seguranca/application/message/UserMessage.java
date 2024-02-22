package br.ejcb.cfp.seguranca.application.message;

import br.ejcb.cfp.seguranca.common.message.MessageUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public final class UserMessage {
	
	@Inject
	private MessageUtils utils;

	public String alreadyUserForThisLogin() {
		return utils.getProperty("already.user.for.this.login");
	}
	
	public String alreadyUserForThisName() {
		return utils.getProperty("already.user.for.this.name");
	}

	public String alreadyUserForThisEmail() {
		return utils.getProperty("already.user.for.this.email");
	}
	
}
