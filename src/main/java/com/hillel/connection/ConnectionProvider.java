package com.hillel.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    public static Connection provideConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "Sansara-040root");
        } catch (SQLException e) {
            System.err.println("Cannot get connection");
            return null;
        }
    }
}
