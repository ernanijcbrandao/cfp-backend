package br.ejcb.cfp.seguranca.ms.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import br.ejcb.cfp.seguranca.ms.domains.repository.UsuarioRepository;
import br.ejcb.cfp.seguranca.ms.util.ConfiguracaoUtil;
import br.ejcb.cfp.seguranca.ms.util.SegurancaUtil;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class UsuarioService {

	@Inject
	UsuarioRepository repository;
	
	@Inject
	SegurancaUtil segurancaUtil;

	@Inject
	ConfiguracaoUtil configUtil;

	public Uni<List<Usuario>> listarSomenteAtivos() {
		return repository.listarSomenteAtivos();
	}

	public Uni<Usuario> carregar(Long id) {
		return repository.findById(id);
	}

	public Uni<Usuario> carregar(String login) {
		return repository.carregar(login);
	}

	@WithTransaction
	public Uni<Usuario> criar(@Valid @NotNull Usuario entity) {
		return repository.carregar(entity.getLogin())
				.onItem()
				.ifNotNull()
				.failWith(() -> new EntityExistsException())
				.onItem()
				.transformToUni(usuario -> {
					return repository.persistAndFlush(entity
							.withAtivo(Boolean.TRUE)
							.withDataCriacao(LocalDate.now(ZoneId.of("America/Sao_Paulo")))
							.withErrosEmSequencia(0)
							.withChave(segurancaUtil.gerarChave(entity.getLogin(), entity.getNome()))
//							.withBloqueado(Boolean.FALSE)
//							.withSenhaExpirada(Boolean.FALSE)
							);
				});
	}

	@WithTransaction
	public Uni<Usuario> atualizar(@Valid @NotNull Long id, @NotNull Usuario entity) {
		return repository.findById(id)
				.onItem().ifNotNull()
				.transformToUni(usuario -> {
					return repository.persist(usuario
							.withNome(entity.getNome())
							.withPerfil(entity.getPerfil())
							.withAtivo(entity.getAtivo())
							);
				})
				.onItem().ifNull().failWith(() -> new EntityNotFoundException());
	}

	@WithTransaction
	public Uni<Void> excluir(@Valid @NotNull Long id) {
		/* caso exista, apenas inativar - exclusao logica */
		return repository.findById(id)
				.onItem()
				.ifNotNull()
				.transformToUni(usuario -> {
					repository.persist(usuario
							.withAtivo(Boolean.FALSE));
					return Uni.createFrom().voidItem();
				})
				.onItem().ifNull().failWith(() -> new EntityNotFoundException());
	}

	@WithTransaction
	public Uni<Usuario> registrarTentativaAcessoSemSucesso(@Valid @NotNull Usuario entity) {
		entity.setErrosEmSequencia(entity.getErrosEmSequencia()>-1?entity.getErrosEmSequencia()+1:1);
		return repository.persistAndFlush(entity);
	}
	
	@WithTransaction
	public Uni<Usuario> registrarAcessoComSucesso(@Valid @NotNull Usuario entity) {
		return repository.persistAndFlush(entity.zerarContadorErroEmSequencia());
	}
	
}
