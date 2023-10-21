package br.ejcb.cfp.seguranca.ms.rest.dto;

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
public class TokenUsuarioAutenticadoDTO {
	
	private String tokenAcesso;
	private String tokenDadosUsuario;
	private String tokenCadastro;
	
	public static synchronized TokenUsuarioAutenticadoDTO create() {
		return new TokenUsuarioAutenticadoDTO();
	}

}
