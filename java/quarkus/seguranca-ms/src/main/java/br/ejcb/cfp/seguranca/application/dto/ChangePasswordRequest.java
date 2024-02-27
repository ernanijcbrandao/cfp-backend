package br.ejcb.cfp.seguranca.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

	@NotEmpty(message = "O atributo 'password' deve ser informado")
	private String password;
	
	@NotEmpty(message = "O atributo 'newPassword' deve ser informado")
	@Size(min = 8, max = 50, message = "O atributo 'newPassword' deve ser preenchido e possuir um tamanho entre 8 e 50 caracteres")
	private String newPassword;
	
}
