package br.ejcb.cfp.seguranca.ms.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import br.ejcb.cfp.seguranca.ms.rest.dto.TokenUsuarioAutenticadoDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessaoUsuario {

	private Map<String, DadosSessao> cache = new HashMap<>();
	
//	public TokenUsuarioAutenticadoDTO criarSessao(final Usuario usuario) {
//		
//		JsonWeb
//		
//		return TokenUsuarioAutenticadoDTO.create();
//	}
//
//    public void storeToken(String token, long durationInMillis) {
//        long expirationTime = System.currentTimeMillis() + durationInMillis;
//        cache.put(token, expirationTime);
//    }
//
//    public Optional<String> getToken(String token) {
//        Long expirationTime = cache.get(token);
//        if (expirationTime != null && expirationTime >= System.currentTimeMillis()) {
//            return Optional.of(token);
//        } else {
//            cache.remove(token);
//            return Optional.empty();
//        }
//    }
	
}
