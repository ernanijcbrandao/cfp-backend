package br.ejcb.cfp.seguranca.resource.exceptions;

public class BlockException extends Exception {

	private static final long serialVersionUID = -2979219164756425662L;

	private Long code;

	public BlockException() {
		super();
	}

	public BlockException(final Long code) {
		super();
		this.code = code;
	}

	public BlockException(String message) {
		super(message);
	}

	public BlockException(String message, Long code) {
		super(message);
		this.code = code;
	}

	public BlockException(Throwable cause) {
		super(cause);
	}

	public BlockException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public Long getCode() {
		return this.code;
	}
	
}
