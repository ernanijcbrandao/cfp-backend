package br.ejcb.cfp.seguranca.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {

	public String gerarToken(String username, String profile, long expireIn) {
        return Jwt.issuer("https://seguranca-be-ms")
                .upn(username)
                .claim("profile", profile)
                .expiresIn(expireIn)
                .sign();
    }

}
