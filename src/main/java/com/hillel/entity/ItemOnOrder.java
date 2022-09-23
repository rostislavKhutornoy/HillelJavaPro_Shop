package com.hillel.entity;

public class ItemOnOrder {
    private int orderNumber;
    private Product product;
    private int amount;

    public ItemOnOrder() {
    }

    public ItemOnOrder(int orderNumber, Product product, int amount) {
        this.orderNumber = orderNumber;
        this.product = product;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ItemOnOrder{" +
                "id=" + orderNumber +
                ", product=" + product +
                ", amount=" + amount +
                '}';
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int id) {
        this.orderNumber = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
