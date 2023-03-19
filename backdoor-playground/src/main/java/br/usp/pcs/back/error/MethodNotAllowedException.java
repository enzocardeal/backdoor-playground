package br.usp.pcs.back.error;

public class MethodNotAllowedException extends ApplicationException {

    MethodNotAllowedException(int code, String message) {
        super(code, message);
    }
}
