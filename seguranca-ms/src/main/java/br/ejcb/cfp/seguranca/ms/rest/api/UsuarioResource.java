package br.ejcb.cfp.seguranca.ms.rest.api;

import java.util.List;

import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioDTO;
import br.ejcb.cfp.seguranca.ms.service.UsuarioService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/usuarios")
public class UsuarioResource {

	@Inject
	private UsuarioService service;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<UsuarioDTO>> listarUsuariosAtivos() {
        return service.listarUsuariosAtivos();
    }
    
    
    /*
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
