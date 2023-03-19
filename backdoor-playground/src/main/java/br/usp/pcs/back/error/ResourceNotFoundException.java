package br.usp.pcs.back.error;

public class ResourceNotFoundException extends ApplicationException {

    ResourceNotFoundException(int code, String message) {
        super(code, message);
    }
}
