package br.usp.pcs.back.domain.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

public class RegistrationModel {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static record RegistrationResponse(String id){};
}
