package br.ejcb.cfp.seguranca.infra.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.ejcb.cfp.seguranca.application.dto.user.SearchUsersFilter;
import br.ejcb.cfp.seguranca.common.enumeration.UserProfile;
import br.ejcb.cfp.seguranca.domain.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

	public Optional<User> loadByLogin(final String login) {
		return this.find("Upper(login)", login.toUpperCase())
				.firstResultOptional();
	}

	public Optional<User> loadByName(final String name) {
		return this.find("Upper(name)", name.toUpperCase())
				.firstResultOptional();
	}

	public Optional<User> loadByEmail(final String email) {
		return this.find("Upper(email)", email.toUpperCase())
				.firstResultOptional();
	}

	public Optional<User> loadByPublicKey(final String publicKey) {
		return this.find("publicKey", publicKey)
				.firstResultOptional();
	}


	private void filterActiveUser(final SearchUsersFilter filter, final StringBuilder hql, final Parameters params) {
		if (filter.getActive() != null) {
			hql.append("AND u.active = :active ");
			params.and("active", filter.getActive());
		}
	}
	private void filterUserName(final SearchUsersFilter filter, final StringBuilder hql, final Parameters params) {
		if (filter.getName() != null) {
			hql.append("AND UPPER(u.name) LIKE :name ");
			params.and("name", "%" + filter.getName().toUpperCase() + "%");
		}
	}
	private void filterUserProfile(final SearchUsersFilter filter, final StringBuilder hql, final Parameters params) {
		if (filter.getProfile() != null) {
			hql.append("AND u.profile = :profile ");
			params.and("profile", UserProfile.findByName(filter.getProfile()));
		}
	}
	private void filterSystemCode(final SearchUsersFilter filter, final StringBuilder hql, final Parameters params) {
		if (filter.getCodeSystem() != null) {
			hql.append("AND s.code = :codeSystem ");
			params.and("codeSystem", filter.getCodeSystem());
		}
	}
	private List<User> addResultList(Optional<User> user) {
		List<User> resultList = new ArrayList<>();
		if (user.isPresent()) {
			resultList.add(user.get());
		}
		return resultList; 
	}
	public List<User> search(final SearchUsersFilter filter) {
		if (filter.getLogin() != null) {
			return addResultList(loadByLogin(filter.getLogin()));
		} else 
			if (filter.getEmail() != null) {
				return addResultList(loadByEmail(filter.getEmail()));
			} else 
				if (filter.getPublicKey() != null) {
					return addResultList(loadByPublicKey(filter.getPublicKey()));
				}
		
		StringBuilder hql = new StringBuilder("SELECT u FROM User u ")
				.append("LEFT JOIN u.systems s ")
				.append("WHERE 1=1 ");
		Parameters params = new Parameters();
		
		filterActiveUser(filter, hql, params);
		filterUserName(filter, hql, params);
		filterUserProfile(filter, hql, params);
		filterSystemCode(filter, hql, params);
		
		return this.list(hql.toString(), params);
	}

}
