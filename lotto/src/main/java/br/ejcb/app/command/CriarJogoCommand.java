package br.ejcb.app.command;

import picocli.CommandLine.Command;

@Command(name = "criarJogo", mixinStandardHelpOptions = true, description = "Criar um novo jogo")
public class CriarJogoCommand implements Runnable {

	@Override
	public void run() {
		System.out.println("Comando criar jogo!");
	}

}
