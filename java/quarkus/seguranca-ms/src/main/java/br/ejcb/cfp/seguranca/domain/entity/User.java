package br.ejcb.cfp.seguranca.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import br.ejcb.cfp.seguranca.common.enumeration.UserProfile;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sg_user",
	uniqueConstraints = {
			@UniqueConstraint(name="uk_user_name", columnNames = {"name"}),
			@UniqueConstraint(name="uk_user_email", columnNames = {"email"}),
			@UniqueConstraint(name="uk_user_login", columnNames = {"login"}),
			@UniqueConstraint(name="uk_user_publickey", columnNames = {"publicKey"})
})
public class User extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 100, nullable = false)
	private String name;
	
	@Column(insertable = true, updatable = false, length = 50, nullable = false)
	private String login;
	
	@Column(length = 100, nullable = false)
	private String email;
	
	@Column(insertable = true, updatable = false, length = 255, nullable = false)
	private String publicKey;
	
	@Column(insertable = true, updatable = true, nullable = false)
	@Enumerated(EnumType.STRING)
	private UserProfile profile;
	
	@Column(insertable = true, updatable = false, nullable = false)
	private LocalDateTime created;
	@Column(insertable = true, updatable = true, nullable = true)
	private LocalDateTime lastUpdate;
	
	@Column(insertable = true, updatable = true, nullable = true)
	private Boolean active;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<System> systems;
	
	public static synchronized User create() {
		return new User();
	}	

}
