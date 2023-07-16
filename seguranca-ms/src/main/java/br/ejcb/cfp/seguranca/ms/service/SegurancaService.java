package br.ejcb.cfp.seguranca.ms.service;

import static br.ejcb.cfp.seguranca.ms.constants.SegurancaConstants.MESSAGE_LOGIN_SENHA_INVALIDO;
import static br.ejcb.cfp.seguranca.ms.constants.SegurancaConstants.MESSAGE_SENHA_EXPIRADA;
import static br.ejcb.cfp.seguranca.ms.constants.SegurancaConstants.MESSAGE_SENHA_NAO_CADASTRADA;
import static br.ejcb.cfp.seguranca.ms.constants.SegurancaConstants.MESSAGE_USUARIO_BLOQUEADO;
import static br.ejcb.cfp.seguranca.ms.constants.SegurancaConstants.MESSAGE_USUARIO_INATIVO;

import java.time.LocalDateTime;

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
	BloqueioService bloqueioService;

	@Inject
	SegurancaUtil segurancaUtil;
	@Inject
	ConfiguracaoUtil configUtil;
	
	public Uni<UsuarioAutenticadoDTO> autenticar(@Valid AutenticacaoDTO dto) {
		return usuarioService.carregar(dto.getLogin())
				.onItem().ifNull().failWith(() -> new SegurancaException(MESSAGE_LOGIN_SENHA_INVALIDO))
				.onItem().ifNotNull().transformToUni(usuario -> {
					
					if (!Boolean.TRUE.equals(usuario.getAtivo())) {
						usuarioService.registrarTentativaAcessoSemSucesso(usuario);
						return Uni.createFrom().failure(new SegurancaException(MESSAGE_USUARIO_INATIVO));
					}
					
					return bloqueioService.possuiBloqueioAtivo(usuario).onItem().transformToUni(bloqueado -> {
						if (bloqueado) {
							usuarioService.registrarTentativaAcessoSemSucesso(usuario);
							return Uni.createFrom().failure(new SegurancaException(MESSAGE_USUARIO_BLOQUEADO));
						}
						
						return senhaService.carregarSenhaAtiva(usuario)
								.onItem().ifNull().failWith(() -> new SegurancaException(MESSAGE_SENHA_NAO_CADASTRADA))
								.onItem().ifNotNull().transformToUni(senha -> {
									
									// verificar se senha informada eh igual a cadastrada
									if (!senha.getSenha().equals(segurancaUtil.criptografarSenha(dto.getSenha(), 
											dto.getLogin(), usuario.getDataCriacao()))) {
										usuarioService.registrarTentativaAcessoSemSucesso(usuario);
										return Uni.createFrom().failure(new SegurancaException(MESSAGE_LOGIN_SENHA_INVALIDO));
									}
									
									// verificar se senha cadastrada expirou
									if (LocalDateTime.now().isAfter(senha.getDataExpiracao())) {
										usuarioService.registrarTentativaAcessoSemSucesso(usuario);
										return Uni.createFrom().failure(new SegurancaException(MESSAGE_SENHA_EXPIRADA));
									}
									
									// do contrario, tudo ok
									usuarioService.registrarAcessoComSucesso(usuario);
									return Uni.createFrom().item(UsuarioAutenticadoDTO.create()
											.withChave(usuario.getChave())
											.withNome(usuario.getNome())
											.withPerfil(usuario.getPerfil())
											);
									
								});
					});
				});
	}

}
