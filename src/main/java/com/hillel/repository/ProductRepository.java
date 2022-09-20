package com.hillel.repository;

import com.hillel.connection.ConnectionProvider;
import com.hillel.entity.Order;
import com.hillel.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Objects.nonNull;

public class ProductRepository {
    public Product findById(int productId) {
        Connection connection = ConnectionProvider.provideConnection();

        if(nonNull(connection)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from product where id=?")){
                statement.setInt(1, productId);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                return new Product(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("cost"));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
