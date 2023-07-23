package br.ejcb.cfp.seguranca.ms.util;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfiguracaoUtil {

	@ConfigProperty(name = "seguranca.dias.expiracao.senha", defaultValue="0")
	private Integer diasParaExpiracaoSenha;

	@ConfigProperty(name = "seguranca.quantidade.senhas.sem.repeticao", defaultValue="0")
	private Integer quantidadeSenhaSemRepeticao;

	@ConfigProperty(name = "seguranca.bloqueio.erros.sequencia", defaultValue="3")
	private Integer quantidadeErrosEmSequenciaParaBloqueioUsuario;

	@ConfigProperty(name = "seguranca.timeout.usuario.autenticado", defaultValue="30")
	private Integer timeoutUsuarioAutenticado;


	public Integer getDiasParaExpiracaoSenha() {
		return diasParaExpiracaoSenha > 0
				? diasParaExpiracaoSenha : 0;
	}

	public Integer getQuantidadeSenhaSemRepeticao() {
		return quantidadeSenhaSemRepeticao > 0
				? quantidadeSenhaSemRepeticao : 0;
	}

	public Integer getQuantidadeErrosEmSequenciaParaBloqueioUsuario() {
		return quantidadeErrosEmSequenciaParaBloqueioUsuario > 0
				? quantidadeErrosEmSequenciaParaBloqueioUsuario : 3;
	}
	public boolean bloquearPorErrosEmSequencia(int valor) {
		return valor >= getQuantidadeErrosEmSequenciaParaBloqueioUsuario();
	}

	public Integer getTimeoutUsuarioAutenticado() {
		return timeoutUsuarioAutenticado > 0
				? timeoutUsuarioAutenticado : 30;
	}

	
}
