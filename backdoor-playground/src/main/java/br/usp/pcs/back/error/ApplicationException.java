package br.usp.pcs.back.error;

public class ApplicationException extends RuntimeException {

    private final int code;

    public int getCode() {
        return code;
    }

    ApplicationException(int code, String message) {
        super(message);
        this.code = code;
    }
}
