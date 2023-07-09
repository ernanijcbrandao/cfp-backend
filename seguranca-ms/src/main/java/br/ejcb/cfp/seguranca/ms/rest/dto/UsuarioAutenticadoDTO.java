package br.ejcb.cfp.seguranca.ms.rest.dto;

import br.ejcb.cfp.seguranca.ms.domain.enums.Perfil;
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
public class UsuarioAutenticadoDTO {
	
	private String chave;
	private String nome;
	private Perfil perfil;
	
	public static synchronized UsuarioAutenticadoDTO create() {
		return new UsuarioAutenticadoDTO();
	}

}
