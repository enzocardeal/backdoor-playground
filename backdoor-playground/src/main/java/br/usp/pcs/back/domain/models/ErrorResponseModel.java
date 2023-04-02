package br.usp.pcs.back.domain.models;

public class ErrorResponseModel {
    public static record ErrorResponse(int code, String message){};
}
