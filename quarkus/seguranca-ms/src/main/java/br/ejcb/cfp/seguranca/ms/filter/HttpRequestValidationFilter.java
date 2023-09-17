package br.ejcb.cfp.seguranca.ms.filter;

import java.io.IOException;

import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(1)
public class HttpRequestValidationFilter implements ContainerRequestFilter {

	private static final String LOGIN_ENDPOINT = "/api/v1/seguranca/autenticar";
    private static final String LOGOUT_ENDPOINT = "/api/v1/seguranca/desautenticar";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        
        System.out.println("\n\n-> endPoint --> " + path);
        
        if (path.equals(LOGIN_ENDPOINT) || path.equals(LOGOUT_ENDPOINT)) {
            // Ignora a validação do JWT para os endpoints de login e logout
            return;
        }

        JsonWebToken jwt = (JsonWebToken) requestContext.getProperty(JsonWebToken.class.getName());
        if (jwt == null || !jwt.getRawToken().startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        // Aqui você pode realizar a validação do token JWT, como a verificação da assinatura e a validade do token.
        // Caso a validação falhe, você pode retornar um código de status de resposta diferente, como UNAUTHORIZED (401).

        // Exemplo básico apenas para fins de demonstração:
        String token = jwt.getRawToken().substring(7); // Remove "Bearer " do início do token
        if (!validarToken(token)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
    }

    private boolean validarToken(String token) {
        // Implemente a validação do token JWT conforme suas necessidades.
        // Por exemplo, você pode usar uma biblioteca JWT para realizar a validação.
        // Retorna true se o token for válido, caso contrário, retorna false.
        // Aqui, estamos apenas retornando true para fins de demonstração.
        return true;
    }

}
