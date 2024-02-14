package br.ejcb.cfp.seguranca.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class Response<T> {

	private String message;
	
	private T entity;
	
	public static synchronized <T> Response<T> create() {
		return new Response<T>();
	}
	public static synchronized <T> Response<T> create(T entity) {
		return new Response<T>()
				.withEntity(entity);
	}

}
