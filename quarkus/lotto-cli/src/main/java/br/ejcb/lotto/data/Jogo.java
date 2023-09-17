package br.ejcb.lotto.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Jogo {
	
	private Integer id;
	private LocalDate data;
	private String nome;
	private Integer quantidadeNumerosPorSentenca;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private BigDecimal valor;
	
	private Integer[] matriz;
	
	private Sentenca[] sentencas;
	
	public static final synchronized Jogo create() {
		return new Jogo();
	}
}
