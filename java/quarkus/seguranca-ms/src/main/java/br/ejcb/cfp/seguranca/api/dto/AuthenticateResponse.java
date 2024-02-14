package br.ejcb.cfp.seguranca.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthenticateResponse extends CodeMessageResponse {
	
	private String accessToken;
	private String refreshToken;
	
	public static synchronized AuthenticateResponse create() {
		return new AuthenticateResponse();
	}

}
