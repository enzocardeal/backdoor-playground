package br.usp.pcs.back.data.entity;

public class UserEntity {
    String id;
    String username;
    String password;

    protected UserEntity(){}
    public UserEntity(String id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
