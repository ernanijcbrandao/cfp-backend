package br.ejcb.cfp.seguranca.usercase;

import java.util.Optional;

import br.ejcb.cfp.seguranca.application.dto.AuthenticateRequest;
import br.ejcb.cfp.seguranca.application.dto.AuthenticateResponse;
import br.ejcb.cfp.seguranca.application.dto.ValidateRequest;
import br.ejcb.cfp.seguranca.application.exceptions.PasswordException;
import br.ejcb.cfp.seguranca.application.validation.AuthenticateValidation;
import br.ejcb.cfp.seguranca.domain.entity.Block;
import br.ejcb.cfp.seguranca.domain.entity.Password;
import br.ejcb.cfp.seguranca.domain.entity.User;
import br.ejcb.cfp.seguranca.infra.repository.PasswordRepository;
import br.ejcb.cfp.seguranca.infra.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@ApplicationScoped
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordRepository passwordRepository;
	private final BlockService blockService;
	private final JwtService jwtService;
	
	@Inject
	public AuthService(UserRepository userRepository,
			PasswordRepository passwordRepository,
			BlockService blockService,
			JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordRepository = passwordRepository;
		this.blockService = blockService;
		this.jwtService = jwtService;
	}

	@Transactional
	public AuthenticateResponse authenticate(AuthenticateRequest request) throws Exception {
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
		
		AuthenticateResponse response = AuthenticateResponse.create();
		try {
			AuthenticateValidation.validate(password.get(), request);
			
			response.withAccessToken(
					jwtService.generateToken(user.get().getName(),
							user.get().getProfile(),
							"system-code", // TODO 
							"system-name", // TODO
							user.get().getPublicKey().toString(),
							1200l, // TODO 20 min -> 20 * 60 => 1200 seg
							"https://seguranca-be-ms.cfp.ejcb.br/")) // criado access token
			.withRefreshToken(
					jwtService.generateToken(user.get().getName(),
							user.get().getProfile(), 
							"system-code", // TODO 
							"system-name", // TODO 
							user.get().getPublicKey().toString(),
							3600l, // TODO 1 h -> 60 min -> 60 * 60 => 3600 seg
							"https://seguranca-be-ms.cfp.ejcb.br/")); // criado refresh token
	
		} catch (PasswordException e) {
			this.registerInvalidAccessAttempt(password);
			throw e;
		}
		
		return response;
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	void registerInvalidAccessAttempt(Optional<Password> password) {
		if (password.isPresent()) {
			passwordRepository.persistAndFlush(password.get().incrementalInvalidAttempt());
		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	void registerValidAccess(Password password) {
		passwordRepository.persistAndFlush(password.resetInvalidAttempt());
	}

	public String validate(ValidateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
