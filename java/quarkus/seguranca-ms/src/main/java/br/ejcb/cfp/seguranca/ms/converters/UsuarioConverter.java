package br.ejcb.cfp.seguranca.ms.converters;

import br.ejcb.cfp.seguranca.ms.domain.model.Usuario;
import br.ejcb.cfp.seguranca.ms.rest.dto.NovoUsuarioDTO;
import br.ejcb.cfp.seguranca.ms.rest.dto.UsuarioDTO;

public final class UsuarioConverter {
	
	public static UsuarioDTO toDTO(Usuario entity) {
		return UsuarioDTO.builder()
				.id(entity.getId())
				.nome(entity.getNome())
				.login(entity.getLogin())
				.dataCriacao(entity.getDataCriacao())
				.ativo(entity.getAtivo())
				.perfil(entity.getPerfil())
				.build();
	}
	
	public static Usuario toEntity(NovoUsuarioDTO dto) {
		return Usuario.builder()
				.nome(dto.getNome())
				.login(dto.getLogin())
				.perfil(dto.getPerfil())
				.build();
	}

	public static Usuario toEntity(UsuarioDTO dto) {
		return Usuario.builder()
				.id(dto.getId())
				.nome(dto.getNome())
				.ativo(dto.getAtivo())
				.login(dto.getLogin())
				.perfil(dto.getPerfil())
				.build();
	}
	
	private UsuarioConverter() {
	}

}
