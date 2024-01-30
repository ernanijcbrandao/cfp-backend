package br.ejcb.cfp.seguranca.resource.exceptions;

public class AuthenticateException extends Exception {

	private static final long serialVersionUID = -2979219164756425662L;
	
	private Long code;

	public AuthenticateException() {
		super();
	}

	public AuthenticateException(final Long code) {
		super();
		this.code = code;
	}

	public AuthenticateException(String message) {
		super(message);
	}

	public AuthenticateException(String message, Long code) {
		super(message);
		this.code = code;
	}

	public AuthenticateException(Throwable cause) {
		super(cause);
	}

	public AuthenticateException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public Long getCode() {
		return this.code;
	}

}
