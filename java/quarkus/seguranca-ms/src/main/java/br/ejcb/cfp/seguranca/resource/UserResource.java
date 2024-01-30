package br.ejcb.cfp.seguranca.resource;

import br.ejcb.cfp.seguranca.resource.dto.UserFilter;
import br.ejcb.cfp.seguranca.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/users")
public class UserResource {
	
	private UserService service;
	
	@Inject
	public UserResource(UserService service) {
		this.service = service;
	}

    @GET
    @Path("/{userId}/userid")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadById(@PathParam("userId") String userId) {
    	return service.load(UserFilter.create().withUserId(userId));
    }

    @GET
    @Path("/{login}/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String loadByLogin(@PathParam("login") String login) {
    	return service.load(UserFilter.create().withLogin(login));
    }

}
