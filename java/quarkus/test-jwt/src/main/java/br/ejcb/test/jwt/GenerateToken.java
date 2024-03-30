package br.ejcb.test.jwt;

import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.microprofile.jwt.Claims;

import io.smallrye.jwt.build.Jwt;

public class GenerateToken {

	/**
     * Generate JWT token
     */
    public static String generate() {
        String token =
           Jwt.issuer("https://test-jwy.ejcb.br/") 
             .upn("jdoe@quarkus.io") 
             .groups(new HashSet<>(Arrays.asList("User", "Admin"))) 
             .claim(Claims.birthdate.name(), "2001-07-13")
            //.expiresAt(60)
           .sign();
        
        System.out.println(token);
        return token;
    }
}
