package br.usp.pcs.back.domain.models;

import java.util.Optional;

public class LoginRequest {
    private String username;
    private String password;
    private Optional<Boolean> debug;

    protected LoginRequest(){};

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest(String username, String password, Optional<Boolean> debug) {
        this.username = username;
        this.password = password;
        this.debug = debug;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getDebug() {
        if(debug != null){
            return debug.get();
        }
        return false;
    }
}
