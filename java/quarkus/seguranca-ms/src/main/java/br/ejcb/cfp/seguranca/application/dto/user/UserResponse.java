package br.ejcb.cfp.seguranca.application.dto.user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.ejcb.cfp.seguranca.common.enumeration.UserProfile;
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
@JsonInclude(Include.NON_NULL)
public class UserResponse {

	private Long id;
	
	private String name;
	private String email;
	private String login;
	private String publikey;
	
	private UserProfile profile;
	
	private LocalDateTime created;
	private LocalDateTime lastUpdate;
	
	private Boolean active;
	
	public static synchronized UserResponse create() {
		return new UserResponse();
	}

}
