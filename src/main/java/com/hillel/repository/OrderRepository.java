package com.hillel.repository;

import com.hillel.connection.ConnectionProvider;
import com.hillel.entity.ItemOnOrder;
import com.hillel.entity.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class OrderRepository {
    public List<Order> findAll() {
        Connection connection = ConnectionProvider.provideConnection();

        if(nonNull(connection)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from shop.order")){
                List<Order> result = new ArrayList<>();
                ResultSet resultSet = statement.executeQuery();

                ItemOnOrderRepository repository = new ItemOnOrderRepository();
                while (resultSet.next()) {
                    result.add(new Order(resultSet.getInt("id"),
                            resultSet.getInt("order_number"),
                            repository.findById(resultSet.getInt("id")),
                            resultSet.getDate("receipt_date").toLocalDate()));
                }
                return result;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return List.of();
    }

    public Order findByNumber(int orderNumber) {
        List<Order> allOrders = findAll()
                .stream()
                .filter(order -> order.getOrderNumber() == orderNumber)
                .toList();
        if (allOrders.size() > 0) {
            return allOrders.get(0);
        } else {
            return null;
        }
    }

    public double orderCost(int orderNumber) {
        List<ItemOnOrder> itemsOnOrder = findByNumber(orderNumber).getItemsOnOrder();
        double totalCost = 0;
        for (ItemOnOrder item : itemsOnOrder) {
            totalCost += item.getProduct().getCost() * item.getAmount();
        }
        return totalCost;
    }
}
