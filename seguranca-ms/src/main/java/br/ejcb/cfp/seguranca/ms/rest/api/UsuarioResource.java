package br.ejcb.cfp.seguranca.ms.rest.api;

import java.util.List;

import br.ejcb.cfp.seguranca.ms.converters.UsuarioConverter;
import br.ejcb.cfp.seguranca.ms.rest.dto.NovoUsuarioDTO;
import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioDTO;
import br.ejcb.cfp.seguranca.ms.service.UsuarioService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/usuarios")
public class UsuarioResource {

	@Inject
	private UsuarioService service;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<UsuarioDTO>> listarSomenteAtivos() {
        return service.listarSomenteAtivos();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UsuarioDTO> carregar(final Long id) {
        return service.carregar(id);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UsuarioDTO> incluir(@Valid @NotNull final NovoUsuarioDTO dto) {
    	return service.gravar(UsuarioConverter.toEntity(dto));
    }

    /*
     * carregar por id
     * carregar por login **
     * listar usuarios
     * incluir usuarios
     * alterar usuarios
     * excluir/inativar usuario
     * 
     * autenticar usuario
     * gerar senha provisoria
     * alterar senha usuario
     * bloquear usuario
     * desbloquear usuario
     */
    

}
