package br.ejcb.cfp.seguranca.config;

import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "seguranca-be-ms")
public class ConfigParams {

	public Integer noRepeatPasswords;
	public String secretPhrase;
	public String timeoutAccessToken;
	public String timeoutRefreshToken;
	public Integer passwordExpirationTime;
	public Integer limitUnsuccessfulAccessAttempts;
	public Integer passwordLockExpirationTime;
	
}
