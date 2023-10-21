package br.ejcb.cfp.seguranca.ms.rest.api;

import java.util.List;
import java.util.stream.Collectors;

import br.ejcb.cfp.seguranca.ms.converters.UsuarioConverter;
import br.ejcb.cfp.seguranca.ms.rest.dto.NovoUsuarioDTO;
import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioDTO;
import br.ejcb.cfp.seguranca.ms.service.UsuarioService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/usuarios")
public class UsuarioResource {

	@Inject
	private UsuarioService service;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Uni<List<UsuarioDTO>> listarSomenteAtivos() {
        return service.listarSomenteAtivos().map(entityList -> entityList.stream()
				.map(UsuarioConverter::toDTO)
				.collect(Collectors.toList()));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Uni<UsuarioDTO> carregar(final Long id) {
        return service.carregar(id).map(UsuarioConverter::toDTO);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Uni<UsuarioDTO> criar(@Valid @NotNull final NovoUsuarioDTO dto) {
    	return service.criar(UsuarioConverter.toEntity(dto))
				.map(UsuarioConverter::toDTO);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Uni<UsuarioDTO> atualizar(@Valid @NotNull Long id, @Valid @NotNull final UsuarioDTO dto) {
    	return service.atualizar(id, UsuarioConverter.toEntity(dto))
				.map(UsuarioConverter::toDTO);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Uni<Void> excluir(@Valid @NotNull Long id) {
    	return service.excluir(id);
    }

}
