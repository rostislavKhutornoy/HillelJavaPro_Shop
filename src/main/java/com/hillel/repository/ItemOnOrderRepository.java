package com.hillel.repository;

import com.hillel.connection.ConnectionProvider;
import com.hillel.entity.ItemOnOrder;
import com.hillel.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class ItemOnOrderRepository {
    public List<ItemOnOrder> findById(int itemOnOrderId) {
        Connection connection = ConnectionProvider.provideConnection();

        if(nonNull(connection)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from item_on_order where id=?")){
                statement.setInt(1, itemOnOrderId);
                ResultSet resultSet = statement.executeQuery();

                List<ItemOnOrder> result = new ArrayList<>();
                ProductRepository productRepository = new ProductRepository();
                while (resultSet.next()) {
                    result.add(new ItemOnOrder(resultSet.getInt("id"),
                            productRepository.findById(resultSet.getInt("product_id")),
                            resultSet.getInt("amount")));
                }
                return result;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
