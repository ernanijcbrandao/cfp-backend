package br.ejcb.cfp.seguranca.api;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.ejcb.cfp.seguranca.application.dto.AuthenticateRequest;
import br.ejcb.cfp.seguranca.application.dto.AuthenticateResponse;
import br.ejcb.cfp.seguranca.application.dto.ValidateRequest;
import br.ejcb.cfp.seguranca.application.exceptions.AuthenticateException;
import br.ejcb.cfp.seguranca.application.exceptions.BlockException;
import br.ejcb.cfp.seguranca.application.exceptions.PasswordException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.usercase.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/security")
@ApplicationScoped
public class AuthenticateResource {
	
	AuthService authService;
	
	@Inject
	public AuthenticateResource(AuthService service) {
		this.authService = service;
	}

	@PermitAll
    @POST
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(AuthenticateRequest request) {
		AuthenticateResponse response = AuthenticateResponse.create();
		Status status = Status.OK;
        try {
        	response = authService.authenticate(request);
		} catch (ValidationException e) {
			response.setMessage(e.getMessage());
			status = Status.BAD_REQUEST;
		} catch (AuthenticateException e) {
			response.withCode(e.getCode())
				.withMessage(e.getMessage());
			status = Status.FORBIDDEN;
		} catch (PasswordException e) {
			response.withCode(e.getCode())
				.withMessage(e.getMessage());
			status = Status.FORBIDDEN;
		} catch (BlockException e) {
			response.withCode(e.getCode())
				.withMessage(e.getMessage());
			status = Status.FORBIDDEN;
		} catch (Exception e) {
			response.withMessage(e.getMessage());
			status = Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(status)
				.entity(response)
				.build();
    }
	
	@Inject
	JsonWebToken jwt;
    
	@PermitAll
    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public String validate(ValidateRequest request) {
        return authService.validate(request);
    }
    
}
