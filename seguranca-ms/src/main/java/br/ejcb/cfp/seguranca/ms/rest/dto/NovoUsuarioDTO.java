package br.ejcb.cfp.seguranca.ms.rest.dto;


import static br.ejcb.cfp.seguranca.ms.constants.UsuarioConstants.CONSTRAINT_FIELD_LOGIN_SIZE;
import static br.ejcb.cfp.seguranca.ms.constants.UsuarioConstants.CONSTRAINT_FIELD_NOME_SIZE;
import static br.ejcb.cfp.seguranca.ms.constants.UsuarioConstants.CONSTRAINT_FIELD_PERFIL_NOT_NULL;

import br.ejcb.cfp.seguranca.ms.domain.enums.Perfil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NovoUsuarioDTO {

	@Size(min = 5, max = 100, message = CONSTRAINT_FIELD_NOME_SIZE)
	private String nome;
	
	@Size(min = 2, max = 50, message = CONSTRAINT_FIELD_LOGIN_SIZE)
	private String login;
	
	@NotNull(message = CONSTRAINT_FIELD_PERFIL_NOT_NULL)
	private Perfil perfil;

	public static synchronized NovoUsuarioDTO create() {
		return new NovoUsuarioDTO();
	}
	
}
