package br.ejcb.cfp.seguranca.ms.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import br.ejcb.cfp.seguranca.ms.converters.UsuarioConverter;
import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import br.ejcb.cfp.seguranca.ms.domains.repository.UsuarioRepository;
import br.ejcb.cfp.seguranca.ms.rest.dto.NovoUsuarioDTO;
import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioDTO;
import br.ejcb.cfp.seguranca.ms.util.SegurancaUtil;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class UsuarioService {

	@Inject
	UsuarioRepository repository;

	public Uni<List<UsuarioDTO>> listarSomenteAtivos() {
		return repository.listarSomenteAtivos()
				.map(entityList -> entityList.stream()
						.map(UsuarioConverter::toDTO)
						.collect(Collectors.toList()));
	}

	public Uni<UsuarioDTO> carregar(Long id) {
		return repository.findById(id).map(UsuarioConverter::toDTO);
	}

	public Uni<UsuarioDTO> carregar(String login) {
		return repository.carregar(login).map(UsuarioConverter::toDTO);
	}

	@WithTransaction
	public Uni<UsuarioDTO> criar(@Valid @NotNull NovoUsuarioDTO dto) {
		Usuario entity = UsuarioConverter.toEntity(dto);
		
		return repository.persistAndFlush(entity.withAtivo(Boolean.TRUE)
				.withDataCriacao(LocalDate.now(ZoneId.of("America/Sao_Paulo")))
				.withChave(SegurancaUtil.gerarChave(entity.getLogin(), entity.getNome())))
				.map(UsuarioConverter::toDTO);
	}

	@WithTransaction
	public Uni<UsuarioDTO> atualizar(@Valid @NotNull Long id, @NotNull UsuarioDTO dto) {
//		Usuario entity = repository.findById(id).
//                .orElseThrow(NotFoundException::new);
//        produto.nome = produtoDTO.nome;
//        produto.descricao = produtoDTO.descricao;
//        produto.preco = produtoDTO.preco;
//        produtoRepository.persist(produto);
//        return toDTO(produto);
				
		return null;
//		return repository.persistAndFlush(entity.withAtivo(Boolean.TRUE)
//				.withDataCriacao(LocalDate.now(ZoneId.of("America/Sao_Paulo")))
//				.withChave(SegurancaUtil.gerarChave(entity.getLogin(), entity.getNome())))
//				.map(UsuarioConverter::toDTO);
	}
	
}
