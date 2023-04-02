package br.usp.pcs.back.data.dbconn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConn {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres?stringtype=unspecified";
    private static final String user = "postgres";
    private static final String password = "postgres";

    public static Connection connect(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return conn;
    }
}
