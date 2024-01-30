package br.ejcb.cfp.seguranca.resource.dto;

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
public class ValidateResponse extends CodeMessageResponse {
	
	private String accessToken;

}
