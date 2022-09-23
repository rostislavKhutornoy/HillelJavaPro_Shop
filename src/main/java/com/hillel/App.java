package com.hillel;

import com.hillel.service.ShopService;


public class App
{
    public static void main(String[] args) {
        ShopService service = new ShopService();
        // Вывести полную информацию о заданном заказе.
        System.out.println(service.orderInfo(2));
        // Вывести номера заказов, сумма которых не превосходит заданную, и количество различных товаров равно заданному.
        System.out.println(service.orderNumbers(100, 2));
        // Вывести номера заказов, содержащих заданный товар.
        System.out.println(service.hasProduct("Apple"));
        // Вывести номера заказов, не содержащих заданный товар и поступивших в течение текущего дня.
        System.out.println(service.hasntProductToday("Orange"));
        // Сформировать новый заказ, состоящий из товаров, заказанных в текущий день.
        //service.newOrderFromOrderedToday();
        // Удалить все заказы, в которых присутствует заданное количество заданного товара.
        //service.deleteOrderWith("Apple", 10);
    }
}
