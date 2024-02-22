package br.ejcb.cfp.seguranca.api;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.jboss.resteasy.reactive.RestResponse.Status;

import br.ejcb.cfp.seguranca.application.dto.CreateUserRequest;
import br.ejcb.cfp.seguranca.application.dto.SearchUsersFilter;
import br.ejcb.cfp.seguranca.application.dto.Response;
import br.ejcb.cfp.seguranca.application.dto.UserResponse;
import br.ejcb.cfp.seguranca.application.exceptions.NotFoundException;
import br.ejcb.cfp.seguranca.application.exceptions.UseCaseException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.application.usecase.user.ICreateUserUseCase;
import br.ejcb.cfp.seguranca.application.usecase.user.ISearchUsersUseCase;
import br.ejcb.cfp.seguranca.application.usecase.user.ILoadUserByIdUseCase;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("cfp/v1/users")
public class UserResource {
	
	ICreateUserUseCase createUseCase;
	ILoadUserByIdUseCase loadUserByIdUseCase;
	ISearchUsersUseCase listUsersUseCase;

	@Inject
	public UserResource(ICreateUserUseCase createUseCase,
			ILoadUserByIdUseCase loadUserByIdUseCase,
			ISearchUsersUseCase listUsersUseCase) {
		this.createUseCase = createUseCase;
		this.loadUserByIdUseCase = loadUserByIdUseCase;
		this.listUsersUseCase = listUsersUseCase;
	}

	@PermitAll
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Response<UserResponse>> create(CreateUserRequest request) {
		RestResponse<Response<UserResponse>> result = null;
		Response<UserResponse> response = Response.create();
		
		try {
			result = ResponseBuilder.create(Status.CREATED, 
					response.withEntity(createUseCase.create(request))
					).build();
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

	@PermitAll
	@Path("/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Response<UserResponse>> loadById(Long userId) {
		RestResponse<Response<UserResponse>> result = null;
		Response<UserResponse> response = Response.create();
		
		try {
			UserResponse user = this.loadUserByIdUseCase.load(userId);
			result = ResponseBuilder.create(user != null ? Status.OK : Status.NOT_FOUND, 
					response.withEntity(user)
					).build();
		} catch (Exception e) {
			result = ResponseBuilder.create(Status.INTERNAL_SERVER_ERROR,
					response.withMessage(e.getMessage())
					).build();
		}
		
		return result;		
    }


	@PermitAll
	@Path("/search")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Response<List<UserResponse>>> list(@QueryParam("login") String login,
            @QueryParam("email") String email,
            @QueryParam("publicKey") String publicKey,
            @QueryParam("name") String name,
            @QueryParam("profile") String profile,
            @QueryParam("active") Boolean active,
            @QueryParam("codeSystem") String codeSystemUser) {
		
		RestResponse<Response<List<UserResponse>>> resultList = null;
		Response<List<UserResponse>> response = Response.create();
		
		try {
			List<UserResponse> users = this.listUsersUseCase.search(SearchUsersFilter.create()
					.withLogin(login)
					.withEmail(email)
					.withPublicKey(publicKey)
					.withActive(active)
					.withName(name)
					.withProfile(profile)
					.withCodeSystem(codeSystemUser));
					
			resultList = ResponseBuilder.create(users != null && !users.isEmpty()
					? Status.OK : Status.NO_CONTENT, 
					response.withEntity(users)
					).build();
		} catch (NotFoundException e) {
			resultList = ResponseBuilder.create(Status.NOT_FOUND, 
					response
					).build();
		} catch (ValidationException e) {
			resultList = ResponseBuilder.create(Status.BAD_REQUEST, 
					response.withMessage(e.getMessage())
					).build();
		} catch (Exception e) {
			resultList = ResponseBuilder.create(Status.INTERNAL_SERVER_ERROR,
					response.withMessage(e.getMessage())
					).build();
		}
		
		return resultList;		
    }

	
}
