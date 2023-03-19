package br.usp.pcs.back.domain.models;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.sun.net.httpserver.Headers;

public class ResponseModel {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static record Response<T>(T body, Headers headers, StatusCode statusCode){};
}
