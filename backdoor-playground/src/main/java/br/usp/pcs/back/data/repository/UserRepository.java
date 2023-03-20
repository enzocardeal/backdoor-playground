package br.usp.pcs.back.data.repository;

import br.usp.pcs.back.data.entity.UserEntity;
import br.usp.pcs.back.domain.models.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static br.usp.pcs.back.data.dbconn.DbConn.connect;

public class UserRepository {
    public UserEntity create(String username, String password) {
        UUID id = UUID.randomUUID();
        UserEntity userEntity = new UserEntity(id, username, password, Role.DEFAULT);

        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO app_user(user_id, username, password, role) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING;"
            );
            statement.setObject(1, userEntity.getId());
            statement.setString(2, userEntity.getUsername());
            statement.setString(3, userEntity.getPassword());
            statement.setObject(4, userEntity.getRole().toString());
            int status = statement.executeUpdate();
            statement.close();
            conn.close();
            if(status != 0){
                return userEntity;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
