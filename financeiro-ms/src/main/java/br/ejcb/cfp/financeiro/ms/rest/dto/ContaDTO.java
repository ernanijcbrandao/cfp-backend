package br.ejcb.cfp.financeiro.ms.rest.dto;

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
public class ContaDTO {
	
	private Long id;
	private String nome;
	private String descricao;

	public static synchronized ContaDTO create() {
		return new ContaDTO();
	}
	
}
