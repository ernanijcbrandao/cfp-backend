package br.ejcb.cfp.seguranca.application.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import br.ejcb.cfp.seguranca.application.dto.user.SearchUsersFilter;
import br.ejcb.cfp.seguranca.application.dto.user.UserResponse;
import br.ejcb.cfp.seguranca.application.exceptions.NotFoundException;
import br.ejcb.cfp.seguranca.application.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.application.usecase.user.ISearchUsersUseCase;
import br.ejcb.cfp.seguranca.common.enumeration.UserProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SearchUsersUseCase implements ISearchUsersUseCase {

	UserService service;
	ModelMapper modelMapper;
	
	@Inject
	public SearchUsersUseCase(UserService service, ModelMapper modelMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public List<UserResponse> search(SearchUsersFilter filter) throws ValidationException, NotFoundException {
		validateProfileFilter(filter);
		List<UserResponse> resultList = service.search(filter).stream()
				.map(user -> modelMapper.map(user, UserResponse.class))
				.collect(Collectors.toList());
		if (this.isExactSearch(filter) && resultList.isEmpty()) {
			throw new NotFoundException();
		}
		return resultList;
	}
	
	private void validateProfileFilter(SearchUsersFilter filter) throws ValidationException {
		if (filter.getProfile() != null) {
			if (UserProfile.findByName(filter.getProfile()) == null) {
				throw new ValidationException("'profile' inválido");
			}
		}
	}
	
	private boolean isExactSearch(SearchUsersFilter filter) {
		return filter != null
				&& (filter.getEmail() != null 
					|| filter.getPublicKey() != null 
					|| filter.getLogin() != null);
	}

}
