package br.ejcb.app.command;

import picocli.CommandLine.Command;

@Command(name = "listarJogos", mixinStandardHelpOptions = true, description = "Listar todos os jogos existentes")
public class ListarJogosCommand implements Runnable {

	@Override
	public void run() {
		System.out.println("Comando listar!");
	}

}
