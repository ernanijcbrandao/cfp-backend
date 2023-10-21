package br.ejcb.cfp.seguranca.ms.rest.dto;


import static br.ejcb.cfp.seguranca.ms.constants.UsuarioConstants.CONSTRAINT_FIELD_ATIVO_NOT_NULL;
import static br.ejcb.cfp.seguranca.ms.constants.UsuarioConstants.CONSTRAINT_FIELD_ID_NOT_NULL;
import static br.ejcb.cfp.seguranca.ms.constants.UsuarioConstants.CONSTRAINT_FIELD_LOGIN_SIZE;
import static br.ejcb.cfp.seguranca.ms.constants.UsuarioConstants.CONSTRAINT_FIELD_NOME_SIZE;
import static br.ejcb.cfp.seguranca.ms.constants.UsuarioConstants.CONSTRAINT_FIELD_PERFIL_NOT_NULL;

import java.time.LocalDate;

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
public class UsuarioDTO {

	@NotNull(message = CONSTRAINT_FIELD_ID_NOT_NULL)
	private Long id;
	
	@Size(min = 5, max = 100, message = CONSTRAINT_FIELD_NOME_SIZE)
	private String nome;
	
	@Size(min = 2, max = 50, message = CONSTRAINT_FIELD_LOGIN_SIZE)
	private String login;
	
	private LocalDate dataCriacao;
	
	@NotNull(message = CONSTRAINT_FIELD_ATIVO_NOT_NULL)
	private Boolean ativo;
	
	@NotNull(message = CONSTRAINT_FIELD_PERFIL_NOT_NULL)
	private Perfil perfil;

	public static synchronized UsuarioDTO create() {
		return new UsuarioDTO();
	}

}
