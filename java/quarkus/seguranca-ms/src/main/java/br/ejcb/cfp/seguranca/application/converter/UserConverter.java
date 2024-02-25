package br.ejcb.cfp.seguranca.application.converter;

import br.ejcb.cfp.seguranca.application.dto.user.CreateUserRequest;
import br.ejcb.cfp.seguranca.application.dto.user.UpdateUserRequest;
import br.ejcb.cfp.seguranca.application.dto.user.UserResponse;
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

	public static final boolean merge(User entity, UpdateUserRequest request) {
		boolean changes = false;
		
		entity.setName((changes = changes 
				|| (request.getName() != null 
						&& !request.getName().isBlank() 
						&& !request.getName().equals(entity.getName())))
				? request.getName() : entity.getName());
		
		entity.setEmail((changes = changes 
				|| (request.getEmail() != null 
						&& !request.getEmail().isBlank() 
						&& !request.getEmail().equals(entity.getEmail())))
				? request.getEmail() : entity.getEmail());
		
		entity.setProfile((changes = changes 
				|| (request.getProfile() != null 
						&& request.getProfile() != entity.getProfile()))
				? request.getProfile() : entity.getProfile());
		
		return changes;
	}
	
}
