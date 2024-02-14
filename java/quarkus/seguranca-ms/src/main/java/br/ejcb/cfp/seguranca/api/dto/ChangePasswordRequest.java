package br.ejcb.cfp.seguranca.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

	private String password;
	private String newPassword;
	
}
