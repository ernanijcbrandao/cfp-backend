package br.ejcb.cfp.seguranca.application.exceptions;

public class UseCaseException extends Exception {

	private static final long serialVersionUID = -2979219164756425662L;

	public UseCaseException() {
		super();
	}

	public UseCaseException(String message) {
		super(message);
	}

	public UseCaseException(Throwable cause) {
		super(cause);
	}

	public UseCaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public UseCaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
