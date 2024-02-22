package br.ejcb.cfp.seguranca.application.converter;

import br.ejcb.cfp.seguranca.application.dto.CreateUserRequest;
import br.ejcb.cfp.seguranca.application.dto.UserResponse;
import br.ejcb.cfp.seguranca.domain.entity.User;

public class UserConverter {

	public static final User toEntity(CreateUserRequest request) {
		return User.create()
				.withName(request.getName())
				.withEmail(request.getEmail())
				.withLogin(request.getLogin())
				.withProfile(request.getProfile())
				;
	}
	
	public static final UserResponse toResponse(User entity) {
		return UserResponse.create()
				.withId(entity.getId())
				.withName(entity.getName())
				.withLogin(entity.getLogin())
				.withEmail(entity.getEmail())
				.withPublikey(entity.getPublicKey())
				.withProfile(entity.getProfile())
				.withCreated(entity.getCreated())
				.withLastUpdate(entity.getLastUpdate())
				.withActive(entity.getActive());
	}

}
