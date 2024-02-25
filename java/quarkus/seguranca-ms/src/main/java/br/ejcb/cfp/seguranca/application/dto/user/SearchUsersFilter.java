package br.ejcb.cfp.seguranca.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchUsersFilter {

	private String login;
	private String email;
	private String publicKey;
	
	private String name;
	private String profile;
	private Boolean active;
	
	private String codeSystem;

	public static synchronized SearchUsersFilter create() {
		return new SearchUsersFilter();
	}

}
