package br.ejcb.lotto.app;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import br.ejcb.lotto.command.IncluirJogoCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@SpringBootApplication
@Configurable
@ComponentScan(basePackages = "br.ejcb.lotto")
@Command(name = "jogo-cli",
		mixinStandardHelpOptions = false, 
		showEndOfOptionsDelimiterInUsageHelp = true,
		addMethodSubcommands = true,
		subcommands = {
				IncluirJogoCommand.class
			}
		)
public class LottoCliApplication { //implements CommandLineRunner {

	public static void main(String[] args) {
        int exitCode = new CommandLine(new LottoCliApplication()).execute(args);
        System.out.println("\n\nlotto-cli finalizado\nexitCode="+exitCode+"\n");
        System.exit(exitCode);
	}

}
