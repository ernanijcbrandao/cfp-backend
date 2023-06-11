package br.ejcb.cfp.seguranca.ms.domain.enums;

import lombok.Getter;

@Getter
public enum Perfil {
	
	ADMIN ("Administrador")
	, NORMAL ("Normal")
	;
	
	private String descricao;
	
	Perfil(String descricao) {
		this.descricao = descricao;
	}

}
