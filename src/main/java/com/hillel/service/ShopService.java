package com.hillel.service;

import com.hillel.connection.ConnectionProvider;
import com.hillel.entity.Order;
import com.hillel.repository.OrderRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.hillel.repository.BaseRepository.closeConnection;
import static java.util.Objects.nonNull;

public class ShopService {
    private final OrderRepository orderRepository;

    public ShopService() {
        this.orderRepository = new OrderRepository();
    }

    public Order orderInfo(int orderNumber) {
        return orderRepository.findByNumber(orderNumber);
    }

    public List<Integer> orderNumbers(double maxTotalCost, int quantityDifferentProducts) {
        Connection connection = ConnectionProvider.provideConnection();
        if(nonNull(connection)) {
            try (PreparedStatement statement = connection.prepareStatement("""
                    SELECT item_on_order.order_number, SUM(cost * amount) AS sum_cost,\s
                    COUNT(DISTINCT item_on_order.product_id) AS product_count
                    FROM shop.order
                    JOIN item_on_order ON shop.order.order_number = item_on_order.order_number
                    JOIN product ON product_id = product.id
                    GROUP BY order_number
                    HAVING sum_cost <= ? AND product_count = ?
                    """)){
                statement.setDouble(1, maxTotalCost);
                statement.setInt(2, quantityDifferentProducts);
                ResultSet resultSet = statement.executeQuery();
                List<Integer> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(resultSet.getInt("order_number"));
                }
                return result;
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                closeConnection(connection);
            }
        }
        return List.of();
    }

    public List<Integer> hasProduct(String productName) {
        return findProductNumberByNamePattern(productName, """
                SELECT DISTINCT order_number FROM item_on_order
                JOIN product ON item_on_order.product_id = product.id
                WHERE product.name = ?
                """);
    }

    public List<Integer> hasntProductToday(String productName) {
        return findProductNumberByNamePattern(productName, """
                SELECT item_on_order.order_number FROM item_on_order
                JOIN shop.order ON item_on_order.order_number = shop.order.order_number
                WHERE item_on_order.order_number NOT IN (SELECT order_number FROM item_on_order
                JOIN product ON item_on_order.product_id = product.id
                WHERE name = ?)
                AND receipt_date BETWEEN Addtime(Curdate(), '00:00:00') AND Addtime(Curdate(), '23:59:59')
                """);
    }

    public void newOrderFromOrderedToday() {
        sqlUpdate("""
                INSERT INTO item_on_order(item_on_order.order_number, item_on_order.product_id, item_on_order.amount)
                SELECT MAX(shop.order.order_number) + 1  AS order_number, item_on_order.product_id, SUM(item_on_order.amount) / 2 AS amount
                FROM item_on_order, shop.order
                WHERE item_on_order.product_id IN (SELECT DISTINCT item_on_order.product_id FROM shop.order
                WHERE shop.order.receipt_date BETWEEN Addtime(Curdate(), '00:00:00') AND Addtime(Curdate(), '23:59:59'))
                GROUP BY item_on_order.product_id
                """);
        sqlUpdate("""
                INSERT INTO shop.order (shop.order.order_number, shop.order.receipt_date)
                SELECT MAX(shop.order.order_number) + 1, CURDATE()
                FROM shop.order
                """);
    }

    public void deleteOrderWith(String productName, int amount) {
        Connection connection = ConnectionProvider.provideConnection();
        if(nonNull(connection)) {
            try (PreparedStatement statement = connection.prepareStatement("""
                DELETE shop.order, item_on_order FROM shop.order
                JOIN item_on_order ON shop.order.order_number = item_on_order.order_number
                JOIN product ON item_on_order.product_id = product.id
                WHERE product.name = ?
                AND item_on_order.amount = ?
                """)){
                statement.setString(1, productName);
                statement.setInt(2, amount);
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                closeConnection(connection);
            }
        }
    }

    private List<Integer> findProductNumberByNamePattern(String productName, String sql) {
        Connection connection = ConnectionProvider.provideConnection();
        if(nonNull(connection)) {
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, productName);
                ResultSet resultSet = statement.executeQuery();
                List<Integer> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(resultSet.getInt("order_number"));
                }
                return result;
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                closeConnection(connection);
            }
        }
        return List.of();
    }

    private void sqlUpdate(String sql) {
        Connection connection = ConnectionProvider.provideConnection();
        if(nonNull(connection)) {
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                closeConnection(connection);
            }
        }
    }
}
