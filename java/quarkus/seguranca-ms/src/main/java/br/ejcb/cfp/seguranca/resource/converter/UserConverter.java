package br.ejcb.cfp.seguranca.resource.converter;

import br.ejcb.cfp.seguranca.api.dto.CreateUserRequest;
import br.ejcb.cfp.seguranca.api.dto.UserResponse;
import br.ejcb.cfp.seguranca.domain.User;

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
