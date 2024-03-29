package br.usp.pcs.back.domain.models;

public enum StatusCode {
    OK(200),
    CREATED(201),
    ACCEPTED(202),

    BAD_REQUEST(400),
    METHOD_NOT_ALLOWED(405);

    private int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
