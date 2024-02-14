package br.ejcb.cfp.seguranca.api.dto;

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
public class UserFilter {
	
	private Long userId;
	private String login;
	
	public static synchronized UserFilter create() {
		return new UserFilter();
	}

}
