package br.ejcb.cfp.seguranca.ms.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SegurancaUtil {

	public String criptografarSenha(final String valor, String frase, LocalDate data) {
		return criptografar(valor, frase, data.atStartOfDay(), false);
	}
	public String gerarChave(final String valor, String frase) {
		return criptografar(valor, frase, LocalDateTime.now(), true);
	}
	
	private final Random random = new Random(Calendar.getInstance().get(Calendar.SECOND));
	private String embaralhar(String origem) {
		StringBuilder novoValor = new StringBuilder();
		boolean trocar = false;
		for (int posicao = 0; posicao < origem.length(); posicao++) {
			String caracter = origem.substring(posicao, posicao+1);
			novoValor.append((trocar=!trocar)?caracter.toUpperCase():caracter.toLowerCase());
		}
		return novoValor.toString();
	}
	private String criptografar(final String valor, String frase, LocalDateTime datahora, boolean gerarValorAleatorio) {
		ZonedDateTime zone = datahora.atZone(ZoneId.of("America/Sao_Paulo"));
		String chave = ""
				+ (datahora.format(DateTimeFormatter.ofPattern("yyyy")))
				+ (valor)
				+ zone.toInstant().toEpochMilli()
				+ (frase != null ? frase : "")
				+ (gerarValorAleatorio ? (""+random.nextLong(100000000, 999999999)) : "");
		
		chave = embaralhar(chave);
		
		try {
			
			MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            byte[] bytes = algorithm.digest(chave.getBytes("UTF-8"));

    		StringBuilder chavehex = new StringBuilder();
    		
    		for (byte b : bytes) {
    			chavehex.append(String.format("%02X", 0xFF & b));
    		}
            
			chave = chavehex.toString();
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		
		return chave;
	}
	
	/*
	public static void main(String a[]) {
		SegurancaUtil util = new SegurancaUtil();
		
		LocalDate criacao = LocalDate.of(2023, 1, 1);
		String login = "admintst";
		String nome = "Login Teste Admin";
		String chave = util.gerarChave(login, nome);
		String senha = util.criptografarSenha("teste123", chave, criacao);
		
		System.out.println("\n\nUsuario Admin");
		System.out.println("login = " + login);
		System.out.println("nome = " + nome);
		System.out.println("chave = " + chave);
		System.out.println("senha = " + senha + "\n\n");

		criacao = LocalDate.of(2023, 1, 2);
		login = "logintst2";
		nome = "Login Teste 2";
		chave = util.gerarChave(login, nome);
		senha = util.criptografarSenha("teste123", chave, criacao);
		
		System.out.println("\n\nUsuario Teste 2");
		System.out.println("login = " + login);
		System.out.println("nome = " + nome);
		System.out.println("chave = " + chave);
		System.out.println("senha = " + senha + "\n\n");

		criacao = LocalDate.of(2023, 1, 3);
		login = "logintst3";
		nome = "Login Teste 3";
		chave = util.gerarChave(login, nome);
		senha = util.criptografarSenha("teste123", chave, criacao);
		
		System.out.println("\n\nUsuario Teste 3");
		System.out.println("login = " + login);
		System.out.println("nome = " + nome);
		System.out.println("chave = " + chave);
		System.out.println("senha = " + senha + "\n\n");

	}
	*/

}
