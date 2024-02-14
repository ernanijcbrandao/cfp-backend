package br.ejcb.cfp.seguranca.domain;

import java.time.LocalDateTime;

import br.ejcb.cfp.seguranca.model.domain.UserProfile;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "sg_user")
public class User extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true, name = "UserName", length = 100, nullable = false)
	private String name;
	
	@Column(unique = true, name = "UserLogin", insertable = true, updatable = false, length = 50, nullable = false)
	private String login;
	
	@Column(unique = true, name = "UserEmail", length = 100, nullable = false)
	private String email;
	
	@Column(unique = true, insertable = true, updatable = false, length = 255, nullable = false)
	private String publicKey;
	
	@Column(insertable = true, updatable = true, nullable = false)
	private UserProfile profile;
	
	@Column(insertable = true, updatable = false, nullable = false)
	private LocalDateTime created;
	@Column(insertable = true, updatable = true, nullable = true)
	private LocalDateTime lastUpdate;
	
	@Column(insertable = true, updatable = true, nullable = true)
	private Boolean active;
	
	public static synchronized User create() {
		return new User();
	}	

}
