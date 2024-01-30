package br.ejcb.cfp.seguranca.resource.exceptions;

public class PasswordException extends Exception {

	private static final long serialVersionUID = -2979219164756425662L;
	
	private Long code;

	public PasswordException() {
		super();
	}

	public PasswordException(final Long code) {
		super();
		this.code = code;
	}

	public PasswordException(String message) {
		super(message);
	}

	public PasswordException(String message, Long code) {
		super(message);
		this.code = code;
	}

	public PasswordException(Throwable cause) {
		super(cause);
	}

	public PasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public Long getCode() {
		return this.code;
	}

}
