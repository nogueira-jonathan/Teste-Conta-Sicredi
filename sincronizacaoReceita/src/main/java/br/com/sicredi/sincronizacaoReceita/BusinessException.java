package br.com.sicredi.sincronizacaoReceita;

public class BusinessException extends RuntimeException {
	public BusinessException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}
}
