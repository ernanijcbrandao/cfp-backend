package br.ejcb.cfp.seguranca.resource;

import br.ejcb.cfp.seguranca.resource.dto.AuthenticateRequest;
import br.ejcb.cfp.seguranca.resource.dto.AuthenticateResponse;
import br.ejcb.cfp.seguranca.resource.dto.ValidateRequest;
import br.ejcb.cfp.seguranca.resource.exceptions.AuthenticateException;
import br.ejcb.cfp.seguranca.resource.exceptions.BlockException;
import br.ejcb.cfp.seguranca.resource.exceptions.PasswordException;
import br.ejcb.cfp.seguranca.resource.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/security")
public class AuthenticateResource {
	
	private AuthService service;
	
	@Inject
	public AuthenticateResource(AuthService service) {
		this.service = service;
	}

    @POST
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(AuthenticateRequest request) {
        try {
			service.authenticate(request);
		} catch (ValidationException e) {
			Response.status(Status.BAD_REQUEST)
				.entity(AuthenticateResponse.create().withMessage(e.getMessage()))
				.build();
		} catch (AuthenticateException e) {
			Response.status(Status.FORBIDDEN)
				.entity(AuthenticateResponse.create()
						.withCode(e.getCode())
						.withMessage(e.getMessage()))
				.build();
		} catch (PasswordException e) {
			Response.status(Status.FORBIDDEN)
				.entity(AuthenticateResponse.create()
						.withCode(e.getCode())
						.withMessage(e.getMessage()))
				.build();
		} catch (BlockException e) {
			Response.status(Status.FORBIDDEN)
				.entity(AuthenticateResponse.create()
						.withCode(e.getCode())
						.withMessage(e.getMessage()))
				.build();
		} catch (Exception e) {
			Response.status(Status.INTERNAL_SERVER_ERROR)
			.entity(AuthenticateResponse.create()
					.withMessage(e.getMessage()))
			.build();
		}
		return Response.ok().build();
    }
    
    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public String validate(ValidateRequest request) {
        return service.validate(request);
    }
    
}
