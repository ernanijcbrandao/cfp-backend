package br.ejcb.seguranca.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ejcb.cfp.seguranca.ms.rest.dto.AutenticacaoDTO;
import br.ejcb.cfp.seguranca.ms.service.SegurancaService;
import br.ejcb.seguranca.dto.TokenUsuarioAutenticadoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/seguranca")
public class SegurancaController {

	private SegurancaService service;

	@Autowired
	public SegurancaController(SegurancaService service) {
		this.service = service;
	}

    /*
     * autenticar usuario
     * gerar senha provisoria
     * alterar senha usuario
     * bloquear usuario
     * desbloquear usuario
     * rotina de recuperacao de senha: 
     *     1 -> receber login ou cpf do usuario ou email seguranca;
     *     2 -> validar se usuario existe (login/cpf/email);
     *     3 -> gerar uma senha aleatoria e enviar para e-mail de seguranca
     *     ---
     *     4.1 quando tentativa de acesso com senha temporaria, pedir cadastramento de senha definitiva
     *       
     */
    @PostMapping(name = "/autenticacao", 
    		consumes = MediaType.APPLICATION_JSON_VALUE, 
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TokenUsuarioAutenticadoDTO> autenticar(@Valid @NotNull final AutenticacaoDTO dto) {
    	return service.autenticar(dto);
    }

}
