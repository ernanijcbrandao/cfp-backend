package br.ejcb.cfp.seguranca.ms.session;

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
public class DadosSessao {
	
	private Long expiracao;
	
	private String nomeUsuario;
	private String chave;
	
	public static synchronized DadosSessao create() {
		return new DadosSessao();
	}
	
	// auxiliares
	public boolean expirado(long tempoComparacao) {
		return tempoComparacao <  this.expiracao;
	}

}
