package br.usp.pcs.back.domain.models;

public class GetRequest {
    private String username;
    private String password;

    protected GetRequest(){};

    public GetRequest(String username, String password) {
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
