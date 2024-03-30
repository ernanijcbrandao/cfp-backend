package br.ejcb.cfp.seguranca.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(content = Include.NON_NULL)
public class AuthenticateResponse extends CodeMessageResponse {
	
	private String accessToken;
	private String refreshToken;
	
	public static synchronized AuthenticateResponse create() {
		return new AuthenticateResponse();
	}

}
