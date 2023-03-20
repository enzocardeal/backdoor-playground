package br.usp.pcs.back.domain.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

public class UserResponseModel {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static record RegistrationResponse(String id, String message){};
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static record LoginResponse(String role, String message){};
}
