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
	
	public String userNotFound() {
		return utils.getProperty("user.not.found");
	}
	
	public String userInactive() {
		return utils.getProperty("user.inactive");
	}
	
	public String userActive() {
		return utils.getProperty("user.active");
	}
	
	public String successUserInactivate() {
		return utils.getProperty("success.user.inactivate");
	}
	
	public String successUserActivate() {
		return utils.getProperty("success.user.activate");
	}
	
	public String requestChangePasswordInsufficient() {
		return utils.getProperty("request.change.password.invalid");
	}

	public String passwordInvalid() {
		return utils.getProperty("password.invalid");
	}

	public String newPasswordUsedLastUpdates() {
		return utils.getProperty("newpassword.used.last.updates");
	}
	
	public String successChangePassword() {
		return utils.getProperty("success.change.password");
	}

}
