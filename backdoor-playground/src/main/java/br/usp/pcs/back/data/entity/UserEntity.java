package br.usp.pcs.back.data.entity;

import br.usp.pcs.back.domain.models.Role;

import java.util.UUID;

public class UserEntity {
    UUID id;
    String username;
    String password;
    Role role;

    protected UserEntity(){}
    public UserEntity(UUID id, String username, String password, Role role){
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
