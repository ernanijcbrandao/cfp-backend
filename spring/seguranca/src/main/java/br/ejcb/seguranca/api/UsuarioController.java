package br.ejcb.seguranca.api;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ejcb.cfp.seguranca.ms.converters.UsuarioConverter;
import br.ejcb.cfp.seguranca.ms.rest.dto.NovoUsuarioDTO;
import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioDTO;
import br.ejcb.cfp.seguranca.ms.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

	private UsuarioService service;
	
	@Autowired
	public UsuarioController(UsuarioService service) {
		this.service = service;
	}
	
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<UsuarioDTO> listarSomenteAtivos() {
        return service.listarSomenteAtivos().map(entityList -> entityList.stream()
				.map(UsuarioConverter::toDTO)
				.collect(Collectors.toList()));
    }

    @GetMapping(name = "/{id}", 
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UsuarioDTO> carregar(final Long id) {
        return service.carregar(id).map(UsuarioConverter::toDTO);
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UsuarioDTO> criar(@Valid @NotNull final NovoUsuarioDTO dto) {
    	return service.criar(UsuarioConverter.toEntity(dto))
				.map(UsuarioConverter::toDTO);
    }

    @PutMapping(name = "/{id}", 
    		consumes = MediaType.APPLICATION_JSON_VALUE, 
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UsuarioDTO> atualizar(@Valid @NotNull Long id, @Valid @NotNull final UsuarioDTO dto) {
    	return service.atualizar(id, UsuarioConverter.toEntity(dto))
				.map(UsuarioConverter::toDTO);
    }

    @DeleteMapping(name = "/{id}", 
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> excluir(@Valid @NotNull Long id) {
    	return service.excluir(id);
    }

}
