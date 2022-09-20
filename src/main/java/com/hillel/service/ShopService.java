package com.hillel.service;

import com.hillel.entity.ItemOnOrder;
import com.hillel.entity.Order;
import com.hillel.entity.Product;
import com.hillel.repository.ItemOnOrderRepository;
import com.hillel.repository.OrderRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

public class ShopService {
    private OrderRepository orderRepository;
    private ItemOnOrderRepository itemOnOrderRepository;

    public ShopService() {
        this.orderRepository = new OrderRepository();
        this.itemOnOrderRepository = new ItemOnOrderRepository();
    }

    public Order orderInfo(int orderNumber) {
        return orderRepository.findByNumber(orderNumber);
    }

    public List<Integer> orderNumbers(double maxTotalCost, int quantityDifferentProducts) {
        List<Integer> result = new ArrayList<>();
        List<Order> allOrders = orderRepository.findAll();
        List<Integer> orderNumbers = allOrders.stream().map(Order::getOrderNumber).toList();
        for (Integer orderNumber : orderNumbers) {
            if(orderRepository.findByNumber(orderNumber).getItemsOnOrder().size() == quantityDifferentProducts) {
                if (orderRepository.orderCost(orderNumber) <= maxTotalCost) {
                    result.add(orderNumber);
                }
            }
        }
        return result;
    }

    public List<Integer> hasProduct(String productName) {
        List<Integer> result = new ArrayList<>();
        List<Order> allOrders = orderRepository.findAll();
        List<Integer> orderNumbers = allOrders.stream().map(Order::getOrderNumber).toList();
        for (Integer orderNumber : orderNumbers) {
            List<String> allProductNames = orderRepository.findByNumber(orderNumber)
                    .getItemsOnOrder()
                    .stream()
                    .map(ItemOnOrder::getProduct)
                    .map(Product::getName)
                    .toList()
                    .stream()
                    .filter(name -> name.equals(productName)).toList();;
           if (allProductNames.size() > 0) {
               result.add(orderNumber);
           }
        }
        return result;
    }

    public List<Integer> hasntProductToday(String productName, LocalDate todayDate) {
        List<Order> allOrders = orderRepository.findAll();
        List<Integer> orderNumbers = new ArrayList<>(allOrders.stream().map(Order::getOrderNumber).toList());
        List<Integer> hasProduct = hasProduct(productName);
        for (Integer orderNumber : hasProduct) {
            orderNumbers.remove(orderNumber);
        }
        orderNumbers.removeIf(orderNumber -> !orderRepository.findByNumber(orderNumber)
                .getReceiptDate().isEqual(todayDate));
        return orderNumbers;
    }
}
