package com.hillel.entity;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private int id;
    private int orderNumber;
    private List<ItemOnOrder> itemsOnOrder;
    private LocalDate receiptDate;

    public Order() {
    }

    public Order(int id, int orderNumber, LocalDate receiptDate) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.receiptDate = receiptDate;
    }

    public Order(int id, int orderNumber, List<ItemOnOrder> itemsOnOrder, LocalDate receiptDate) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.itemsOnOrder = itemsOnOrder;
        this.receiptDate = receiptDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNumber=" + orderNumber +
                ", itemsOnOrder=" + itemsOnOrder +
                ", receiptDate=" + receiptDate +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<ItemOnOrder> getItemsOnOrder() {
        return itemsOnOrder;
    }

    public void setItemsOnOrder(List<ItemOnOrder> itemsOnOrder) {
        this.itemsOnOrder = itemsOnOrder;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }
}
