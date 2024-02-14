package br.ejcb.cfp.seguranca.api.dto;

import br.ejcb.cfp.seguranca.model.domain.UserProfile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UpdateUserRequest {

	@NotBlank(message = "O atributo 'name' deve ser informado")
	private String name;
	
	@NotBlank(message = "O atributo 'email' deve ser informado")
	@Email(message = "'email' informado é inválido")
	private String email;
	
	@NotBlank(message = "O atributo 'login' deve ser informado")
	private String login;
	
	private UserProfile profile;
	
}
