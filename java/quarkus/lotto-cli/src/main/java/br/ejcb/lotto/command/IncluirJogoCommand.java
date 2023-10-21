package br.ejcb.lotto.command;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.ejcb.lotto.command.converter.BigDecimalConverter;
import br.ejcb.lotto.command.converter.IntegerConverter;
import br.ejcb.lotto.command.converter.LocalDateConverter;
import br.ejcb.lotto.data.Jogo;
import br.ejcb.lotto.service.JogoService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(name = "incj", 
		description = "Incluir novo jogo", 
		exitCodeOnSuccess = 0, 
		exitCodeOnInvalidInput = 19, 
		separator = "=",
		exitCodeOnExecutionException = 99, 
		showEndOfOptionsDelimiterInUsageHelp = true )
public class IncluirJogoCommand implements Runnable {
	
	private JogoService jogaService = new JogoService();
	
	@Option(names = {"-n","--nome"}, required = true, description = "Nome do jogo")
	private String nome;
	
	@Option(names = {"-d","--data"}, required = true, description = "Data do jogo", type = LocalDate.class ,converter = LocalDateConverter.class)
	private LocalDate data;
	
	@Option(names = {"-ns","--numerosPorSetenca"}, required = true, description = "Quantidade numeros por sentenca", type = Integer.class ,converter = IntegerConverter.class)
	private Integer quantidadeNumerosPorSentenca;

	@Option(names = {"-v","--valorAposta"}, required = true, description = "Valor da aposta", type = BigDecimal.class ,converter = BigDecimalConverter.class)
	private BigDecimal valor;

	@Override
	public void run() {
		jogaService.incluirJogo(Jogo.create()
				.withNome(nome)
				.withData(data)
				.withQuantidadeNumerosPorSentenca(quantidadeNumerosPorSentenca)
				.withValor(valor)
				);
	}

}
