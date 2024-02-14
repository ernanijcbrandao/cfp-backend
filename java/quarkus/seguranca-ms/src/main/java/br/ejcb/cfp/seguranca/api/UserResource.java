package br.ejcb.cfp.seguranca.api;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.RestResponse.Status;

import br.ejcb.cfp.seguranca.api.dto.CreateUserRequest;
import br.ejcb.cfp.seguranca.api.dto.Response;
import br.ejcb.cfp.seguranca.api.dto.UserResponse;
import br.ejcb.cfp.seguranca.application.user.ICreateUserUseCase;
import br.ejcb.cfp.seguranca.resource.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.resource.exceptions.ValidationException;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("cfp/v1/users")
public class UserResource {
	
	ICreateUserUseCase createUseCase;
	
	@Inject
	public UserResource(ICreateUserUseCase createUseCase) {
		this.createUseCase = createUseCase;
	}

	@PermitAll
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Response<UserResponse>> create(CreateUserRequest request) throws ValidationException, UseCaseException {
		RestResponse<Response<UserResponse>> result = null;
		Response<UserResponse> response = Response.create();
		try {
			result = ResponseBuilder.ok(response.withEntity(createUseCase.create(request))).build();
		} catch (ValidationException e) {
			result = ResponseBuilder.create(Status.BAD_REQUEST, 
					response.withMessage(e.getMessage())
					).build();
		} catch (UseCaseException e) {
			result = ResponseBuilder.create(Status.BAD_REQUEST,
					response.withMessage(e.getMessage())
					).build();
		} catch (Exception e) {
			result = ResponseBuilder.create(Status.INTERNAL_SERVER_ERROR,
					response.withMessage(e.getMessage())
					).build();
		}
		return result;
		
    }

}
