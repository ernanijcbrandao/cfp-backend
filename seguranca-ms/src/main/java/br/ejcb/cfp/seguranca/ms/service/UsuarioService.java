package br.ejcb.cfp.seguranca.ms.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import br.ejcb.cfp.seguranca.ms.converters.UsuarioConverter;
import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import br.ejcb.cfp.seguranca.ms.domains.repository.UsuarioRepository;
import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioDTO;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioService {

	@Inject
	UsuarioRepository repository;

	public Uni<List<UsuarioDTO>> listarSomenteAtivos() {
		return repository.listarSomenteAtivos()
				.map(entityList -> entityList.stream()
						.map(UsuarioConverter::toDTO)
						.collect(Collectors.toList()));
	}

	public Uni<UsuarioDTO> carregar(Long id) {
		return repository.findById(id).map(UsuarioConverter::toDTO);
	}

	public Uni<UsuarioDTO> carregar(String login) {
		return repository.carregar(login).map(UsuarioConverter::toDTO);
	}

	@WithTransaction
	public Uni<UsuarioDTO> gravar(Usuario entity) {
		return repository.persistAndFlush(
				entity.withChave(gerarChaveUnica(entity.getLogin()))
				).map(UsuarioConverter::toDTO);
	}
	
	private Random random = new Random(Calendar.getInstance().get(Calendar.SECOND));
	private String gerarChaveUnica(final String login) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMMMdd");
		
		long aleatorio = random.nextLong(100000000, 999999999);
		
		String chave = ""
				+ Calendar.getInstance().getTimeInMillis()
				+ login 
				+ format.format(Calendar.getInstance().getTime())
				+ aleatorio;
		
		System.out.println(chave);
		
		try {
			
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] bytes = algorithm.digest(chave.getBytes("UTF-8"));

    		System.out.println(bytes);
    		
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
	
	public static void main(String [] a) {
		UsuarioService s = new UsuarioService();
		for (int i=0; i<5; i++)
			System.out.println(s.gerarChaveUnica("Teste")+"\n");
	}

}
