package br.ejcb.cfp.seguranca.ms.rest.dto;


import jakarta.validation.constraints.NotNull;
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
public class AutenticacaoDTO {

	@NotNull
	private String login;
	@NotNull
	private String senha;

	public static synchronized AutenticacaoDTO create() {
		return new AutenticacaoDTO();
	}
	
}
