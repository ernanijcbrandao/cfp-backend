package br.ejcb.app;

import br.ejcb.app.command.CriarJogoCommand;
import br.ejcb.app.command.ListarJogosCommand;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(name = "lotto-app", 
		mixinStandardHelpOptions = true, 
		subcommands = {CriarJogoCommand.class, ListarJogosCommand.class}
)
public class LottoApplication {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new LottoApplication()).execute(args);
        System.exit(exitCode);
    }

}

