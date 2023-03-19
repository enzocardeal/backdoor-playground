package br.usp.pcs.back.data.repository;

import br.usp.pcs.back.data.entity.UserEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private static final Map USERS_STORE = new ConcurrentHashMap();

    public UserEntity create(String username, String password) {
        String id = UUID.randomUUID().toString();
        UserEntity userEntity = new UserEntity(id, username, password);
        USERS_STORE.put(username, userEntity);

        return userEntity;
    }
}
