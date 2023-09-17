package br.ejcb.lotto.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ejcb.lotto.data.Jogo;

@Service
public class JogoService {

	@Value("${lotto.data.dir}")
	private String directorioDados;
	private String nomeArquivoDados;
	
	private List<Jogo> jogos;
	private Integer maxid;

	public boolean incluirJogo(Jogo jogo) {
		boolean result = jogo != null 
				&& jogo.getId() == null;
		
		if (result && findByNome(jogo.getNome()) != null) {
			System.out.println("O jogo '" + jogo.getNome() + "' ja existe!");
			result = false;
		}
		
		result = result && this.jogos.add(jogo.withId(newMaxId()));
		
		this.gravar();
		
		return true;
	}
	
	private Jogo findByNome(final String nome) {
		if (nome == null || nome.isEmpty()) {
			return null;
		}
		return this.getJogos().stream()
				.filter(jogo ->nome.equals(jogo.getNome()))
				.findFirst()
				.orElse(null);
	}
	
	private List<Jogo> getJogos() {
		if (this.jogos == null) {
			carregar();
		}
		return this.jogos;
	}
	private void setJogos(List<Jogo> jogos) {
		this.jogos = jogos;
		setMaxId();
	}
	private void setMaxId() {
		if (this.jogos == null || this.jogos.isEmpty()) {
			this.maxid = 0;
		} else {
			final int maxid[] = new int[] {0};
			this.jogos.forEach(jogo -> {
				if (jogo.getId() > maxid[0]) {
					maxid[0] = jogo.getId();
				}
			});
			this.maxid = maxid[0];
		}
	}
	private Integer newMaxId() {
		return ++this.maxid;
	}
	
	private void carregar() {
		ObjectMapper objectMapper = new ObjectMapper();
        List<Jogo> jogos = new ArrayList<>();
        try {
            System.out.println("\n==> DEBUG > " + this.directorioDados);
            this.directorioDados = this.directorioDados != null ? this.directorioDados : "."; 
        	this.nomeArquivoDados = this.directorioDados + "/lotto.data";
        	
            System.out.println("==> DEBUG > " + this.nomeArquivoDados);
            
        	File file = new File(this.nomeArquivoDados);
            System.out.println("==> DEBUG > " + file.getAbsolutePath());
            System.out.println("\n");
        	
            System.out.println("ler arquivo de jogos... [" + file.getAbsolutePath() + "]");
            jogos = objectMapper.readValue(file, 
            		objectMapper.getTypeFactory().constructCollectionType(List.class, Jogo.class));
            System.out.println("carregados jogos gravados... [" + jogos.size() + "]");
            		
        } catch (IOException e) {
            System.out.println("nenhum jogo carregado... [" + e.getMessage() + "]");
        }
        setJogos(jogos);
	}
	
	private boolean gravar() {
		boolean result = true;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(this.nomeArquivoDados), jogos);
        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
            result = false;
        }
        return result;
    }
	
}
