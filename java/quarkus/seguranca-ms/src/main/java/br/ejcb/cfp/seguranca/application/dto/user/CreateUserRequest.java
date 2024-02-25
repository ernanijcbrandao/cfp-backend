package br.ejcb.cfp.seguranca.application.dto.user;

import br.ejcb.cfp.seguranca.common.enumeration.UserProfile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class CreateUserRequest {

	@NotEmpty(message = "O atributo 'name' deve ser informado")
	@Size(min = 10, max = 100, message = "O atributo 'name' deve ser preenchido e possuir um tamanho entre 10 e 100 caracteres")
	private String name;

	@NotEmpty(message = "O atributo 'email' deve ser informado")
	@Email(message = "'email' informado é inválido")
	private String email;

	@NotEmpty(message = "O atributo 'login' deve ser informado")
	@Size(min = 10, max = 50, message = "O atributo 'login' deve ser preenchido e possuir um tamanho entre 10 e 50 caracteres")
	private String login;

	@NotNull(message = "O atributo 'profile' deve ser informado")
	private UserProfile profile;
	
}
