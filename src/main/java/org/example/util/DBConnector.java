package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:./src/main/resources/testat_db.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DBConnector() {

    }

    public static Connection getConnection() {
        return connection;
    }
}
