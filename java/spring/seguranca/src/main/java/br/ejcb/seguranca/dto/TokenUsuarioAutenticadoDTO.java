package br.ejcb.seguranca.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
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
