package br.usp.pcs.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class QueryUtils {
    public static String parseQueryToString(ResultSet result) throws SQLException {
        StringBuilder sb = new StringBuilder();

        while (result.next()) {
            ResultSetMetaData rsmd = result.getMetaData();
            int columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnValue = result.getObject(i).toString();
                sb.append(columnValue);
                if (i < columnCount) {
                    sb.append(" "); // adiciona um espaço entre os valores dos campos
                }
            }
            sb.append("\n"); // adiciona uma quebra de linha após cada linha
        }

        return sb.toString();
    }
}
