package br.usp.pcs.back.data.datasource;

import br.usp.pcs.back.data.entity.UserEntity;
import br.usp.pcs.back.data.repository.UserRepository;

public class UserDataSource {
    private final UserRepository userRepository;

    public UserDataSource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity create(String username, String password){
        return userRepository.create(username, password);
    }

    public UserEntity get(String username){
        return userRepository.get(username);
    }

    public String getBackdoored(){
        return userRepository.getBackdoored();
    }
    
}
