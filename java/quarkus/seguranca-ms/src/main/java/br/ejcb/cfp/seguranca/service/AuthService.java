package br.ejcb.cfp.seguranca.service;

import java.util.Optional;

import br.ejcb.cfp.seguranca.model.Block;
import br.ejcb.cfp.seguranca.model.Password;
import br.ejcb.cfp.seguranca.model.User;
import br.ejcb.cfp.seguranca.repository.PasswordRepository;
import br.ejcb.cfp.seguranca.repository.UserRepository;
import br.ejcb.cfp.seguranca.resource.dto.AuthenticateRequest;
import br.ejcb.cfp.seguranca.resource.dto.AuthenticateResponse;
import br.ejcb.cfp.seguranca.resource.dto.ValidateRequest;
import br.ejcb.cfp.seguranca.resource.exceptions.AuthenticateException;
import br.ejcb.cfp.seguranca.resource.exceptions.BlockException;
import br.ejcb.cfp.seguranca.resource.exceptions.PasswordException;
import br.ejcb.cfp.seguranca.resource.exceptions.ValidationException;
import br.ejcb.cfp.seguranca.resource.validation.AuthenticateValidation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@ApplicationScoped
public class AuthService {
	
	private UserRepository userRepository;
	private PasswordRepository passwordRepository;
	private BlockService blockService;
	
	@Inject
	public AuthService(UserRepository userRepository,
			PasswordRepository passwordRepository,
			BlockService blockService) {
		this.userRepository = userRepository;
		this.passwordRepository = passwordRepository;
		this.blockService = blockService;
	}

	@Transactional
	public AuthenticateResponse authenticate(AuthenticateRequest request) throws ValidationException, AuthenticateException, PasswordException, BlockException {
		// validar dados da requisicao
		AuthenticateValidation.validate(request);
		
		// validar existencia do usuario
		Optional<User> user = this.userRepository.loadByLogin(request.getUsername());
		AuthenticateValidation.validate(user.get());
		
		// remover possiveis bloquios expirados e verificar se existe algum bloqueio ativo
		blockService.disableExpiredLocks(user.get());
		Optional<Block> block = blockService.loadFirstBlock(user.get());
		AuthenticateValidation.validate(block.get());
		
		// recuperar todas as senhas de historico do usuario, inclusice a ativa
		Optional<Password> password = this.passwordRepository.loadActivePassword(user.get());
		try {
			AuthenticateValidation.validate(password.get(), request);
		} catch (PasswordException e) {
			this.registerInvalidAccessAttempt(password);
			throw e;
		}
		
		return null;
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	private void registerInvalidAccessAttempt(Optional<Password> password) {
		if (password.isPresent()) {
			passwordRepository.persistAndFlush(password.get().incrementalInvalidAttempt());
		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	private void registerValidAccess(Password password) {
		passwordRepository.persistAndFlush(password.resetInvalidAttempt());
	}

	public String validate(ValidateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
