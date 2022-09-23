package com.hillel.repository;

import com.hillel.connection.ConnectionProvider;
import com.hillel.entity.ItemOnOrder;
import com.hillel.entity.Order;
import com.hillel.entity.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.hillel.repository.BaseRepository.closeConnection;
import static java.util.Objects.nonNull;

public class OrderRepository {
    public Order findByNumber(int orderNumber) {
        Connection connection = ConnectionProvider.provideConnection();
        if(nonNull(connection)) {
            try (PreparedStatement statement = connection.prepareStatement("""
                    SELECT o.id, o.order_number, o.receipt_date, i.order_number, i.product_id, i.amount, p.id, p.name, p.description, p.cost
                    FROM shop.order o LEFT JOIN item_on_order i ON o.order_number = i.order_number
                    LEFT JOIN product p ON i.product_id = p.id WHERE i.order_number = ?
                    """, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)){
                statement.setInt(1, orderNumber);
                ResultSet resultSet = statement.executeQuery();
                List<ItemOnOrder> itemsOnOrder = new ArrayList<>();
                while (resultSet.next()) {
                    itemsOnOrder.add(new ItemOnOrder(resultSet.getInt(2),
                            new Product(resultSet.getInt("product_id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("description"),
                                    resultSet.getDouble("cost")),
                            resultSet.getInt("amount")));
                }
                resultSet.previous();
                return new Order(resultSet.getInt(1),
                        resultSet.getInt("order_number"),
                        itemsOnOrder,
                        resultSet.getDate("receipt_date").toLocalDate());
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                closeConnection(connection);
            }
        }
        return null;
    }
}
