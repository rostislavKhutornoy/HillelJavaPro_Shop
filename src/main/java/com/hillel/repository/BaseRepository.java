package com.hillel.repository;

import java.sql.Connection;
import java.sql.SQLException;

import static java.util.Objects.nonNull;

public class BaseRepository {
    public static void closeConnection(Connection connection) {
        if(nonNull(connection)) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Cannot close connection");
            }
        }
    }
}
