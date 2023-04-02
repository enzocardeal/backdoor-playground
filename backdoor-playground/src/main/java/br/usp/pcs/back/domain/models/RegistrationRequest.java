package br.usp.pcs.back.domain.models;

public class RegistrationRequest {
    private String username;
    private String password;

    protected RegistrationRequest(){};

    public RegistrationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
