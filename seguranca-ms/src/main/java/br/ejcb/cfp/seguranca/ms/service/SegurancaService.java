package br.ejcb.cfp.seguranca.ms.service;

import static br.ejcb.cfp.seguranca.ms.constants.SegurancaConstants.MESSAGE_SENHA_NAO_CADASTRADA;
import static br.ejcb.cfp.seguranca.ms.constants.SegurancaConstants.MESSAGE_USUARIO_INATIVO;
import static br.ejcb.cfp.seguranca.ms.constants.SegurancaConstants.MESSAGE_USUARIO_BLOQUEADO;
import static br.ejcb.cfp.seguranca.ms.constants.SegurancaConstants.MESSAGE_LOGIN_SENHA_INVALIDO;

import br.ejcb.cfp.seguranca.ms.rest.dto.AutenticacaoDTO;
import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioAutenticadoDTO;
import br.ejcb.cfp.seguranca.ms.service.exceptions.SegurancaException;
import br.ejcb.cfp.seguranca.ms.util.ConfiguracaoUtil;
import br.ejcb.cfp.seguranca.ms.util.SegurancaUtil;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@ApplicationScoped
public class SegurancaService {

	@Inject
	UsuarioService usuarioService;
	@Inject
	SenhaService senhaService;

	@Inject
	SegurancaUtil segurancaUtil;
	@Inject
	ConfiguracaoUtil configUtil;
	
	public Uni<UsuarioAutenticadoDTO> autenticar(@Valid AutenticacaoDTO dto) {
		return usuarioService.carregar(dto.getLogin())
				.onItem().ifNotNull().transformToUni(usuario -> {
					
					if (!Boolean.TRUE.equals(usuario.getAtivo())) {
						return Uni.createFrom().failure(new SegurancaException(MESSAGE_USUARIO_INATIVO));
					}
					
					if (!Boolean.TRUE.equals(usuario.getBloqueado())) {
						return Uni.createFrom().failure(new SegurancaException(MESSAGE_USUARIO_BLOQUEADO));
					}
					
					senhaService.carregarSenhaAtiva(usuario)
							// senha nao configurada
							.onItem().ifNull().failWith(() -> new SegurancaException(MESSAGE_SENHA_NAO_CADASTRADA))
							.onItem().ifNotNull().transformToUni(senha -> {
								// senha inativada
								if (!Boolean.TRUE.equals(senha.getAtivo())) {
									return Uni.createFrom().failure(new SegurancaException(MESSAGE_SENHA_NAO_CADASTRADA));
								}
								
								// senha expirada
								
								return null;
							});
					
					usuarioService.gravarTentativaAcessoSemSucesso(usuario.zerarContadorErroEmSequencia());
					
					return Uni.createFrom().item(UsuarioAutenticadoDTO.create()
							.withChave(usuario.getChave())
							.withNome(usuario.getNome())
							.withPerfil(usuario.getPerfil())
							);
				})
				.onItem().ifNull().failWith(() -> new SegurancaException(MESSAGE_LOGIN_SENHA_INVALIDO));
	}

}
