package br.ejcb.test.resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import br.ejcb.test.jwt.GenerateToken;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/secured")
@RequestScoped
public class TokenSecuredResource {

    @Inject
    JsonWebToken jwt;
    
    @Inject
    @Claim(standard = Claims.birthdate)
    String birthdate;

    @GET()
    @Path("permit-all")
    @PermitAll 
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context SecurityContext ctx) {
        return getResponseString(ctx) + getDataHora(); 
    }
    
    private String getDataHora() {
    	return " - Data/hora: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    
    @GET()
    @Path("generateToken")
    @PermitAll 
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken(@Context SecurityContext ctx) {
        return "Token ->\n" + GenerateToken.generate();
    }
    
    @GET
    @Path("roles-allowed") 
    @RolesAllowed({ "User", "Admin" }) 
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        return getResponseString(ctx) + ", birthdate: " + jwt.getClaim("birthdate").toString() 
        		+ getDataHora(); 
    }
    
    @GET
    @Path("roles-allowed-admin")
    @RolesAllowed("Admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowedAdmin(@Context SecurityContext ctx) {
        return getResponseString(ctx) + ", birthdate: " + birthdate 
        		+ getDataHora();
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) { 
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) { 
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName(); 
        }
        return String.format("hello %s,"
            + " isHttps: %s,"
            + " authScheme: %s,"
            + " hasJWT: %s",
            name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt()); 
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
    
}