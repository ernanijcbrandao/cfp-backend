package br.ejcb.cfp.seguranca.model;

import java.time.LocalDateTime;
import java.util.UUID;

import br.ejcb.cfp.seguranca.model.domain.UserProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(unique = true)
	private String name;
	
	@Column(unique = true, insertable = true, updatable = false)
	private String login;
	
	@Column(unique = true)
	private String email;
	
	@Column(unique = true, insertable = true, updatable = false)
	private UUID publicKey;
	
	private UserProfile profile;
	
	@Column(insertable = true)
	private LocalDateTime created;
	private LocalDateTime lastUpdate;
	
	private Boolean active;
	
	public static synchronized User create() {
		return new User();
	}	

}
