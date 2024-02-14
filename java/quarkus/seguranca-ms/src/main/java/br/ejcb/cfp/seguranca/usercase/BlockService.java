package br.ejcb.cfp.seguranca.usercase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.ejcb.cfp.seguranca.domain.Block;
import br.ejcb.cfp.seguranca.domain.User;
import br.ejcb.cfp.seguranca.repository.BlockRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@ApplicationScoped
public class BlockService {
	
	private BlockRepository repository;
	
	@Inject
	public BlockService(BlockRepository repository) {
		this.repository = repository;
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void disableExpiredLocks(final User user) {
		// recuperar todos os bloqueios ativos
		List<Block> locks = repository.listActiveLocks(user);
		// desabilita os bloqueios expirados
		locks.forEach(block -> {
			if (block.getExpire() != null) {
				boolean expired = block.getExpire().isBefore(LocalDateTime.now());
				if (expired) {
					repository.persistAndFlush(block.withActive(Boolean.FALSE));
				}
			}
		});
	}
	
	public Optional<Block> loadFirstBlock(final User user) {
		return repository.loadFirstActiveLocks(user);
	}

}
