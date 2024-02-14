package br.ejcb.cfp.seguranca.config;

import org.eclipse.microprofile.config.inject.ConfigProperties;

import lombok.Getter;

@Getter
@ConfigProperties(prefix = "seguranca-be-ms")
public class ConfigParams {

	private Integer noRepeatPasswords;
	private String secretPhrase;
	private String timeoutAccessToken;
	private String timeoutRefreshToken;
	private Integer passwordExpirationTime;
	private Integer limitUnsuccessfulAccessAttempts;
	private Integer passwordLockExpirationTime;
	
}
