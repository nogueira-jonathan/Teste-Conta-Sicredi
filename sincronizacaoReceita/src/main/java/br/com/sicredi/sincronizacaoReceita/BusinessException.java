package br.com.sicredi.sincronizacaoReceita;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}