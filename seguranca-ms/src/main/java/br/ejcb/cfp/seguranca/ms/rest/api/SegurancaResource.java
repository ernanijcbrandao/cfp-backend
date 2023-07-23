package br.ejcb.cfp.seguranca.ms.rest.api;

import br.ejcb.cfp.seguranca.ms.rest.dto.AutenticacaoDTO;
import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioAutenticadoDTO;
import br.ejcb.cfp.seguranca.ms.service.SegurancaService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/seguranca")
public class SegurancaResource {

	@Inject
	private SegurancaService service;
	
    @POST
    @Path("/autenticacao")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UsuarioAutenticadoDTO> autenticar(@Valid @NotNull final AutenticacaoDTO dto) {
    	return service.autenticar(dto);
    }

    /*
     * autenticar usuario
     * gerar senha provisoria
     * alterar senha usuario
     * bloquear usuario
     * desbloquear usuario
     */

}
