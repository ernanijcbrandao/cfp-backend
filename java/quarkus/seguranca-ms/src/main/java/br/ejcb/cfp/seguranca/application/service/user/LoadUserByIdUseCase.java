package br.ejcb.cfp.seguranca.application.service.user;

import java.util.Optional;

import br.ejcb.cfp.seguranca.application.converter.UserConverter;
import br.ejcb.cfp.seguranca.application.dto.UserResponse;
import br.ejcb.cfp.seguranca.application.usecase.user.ILoadUserByIdUseCase;
import br.ejcb.cfp.seguranca.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoadUserByIdUseCase implements ILoadUserByIdUseCase {

	private final UserService service;
	
	@Inject
	public LoadUserByIdUseCase(UserService service) {
		this.service = service;
	}
	
	@Override
	public UserResponse load(Long userId) {
		Optional<User> user = this.service.loadById(userId);
		return user.isPresent()
				? UserConverter.toResponse(user.get()) : null;
	}

}
