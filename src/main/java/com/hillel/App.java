package com.hillel;

import com.hillel.repository.ItemOnOrderRepository;
import com.hillel.repository.OrderRepository;
import com.hillel.repository.ProductRepository;
import com.hillel.service.ShopService;

import java.time.LocalDate;


public class App
{
    public static void main(String[] args) {
        ShopService service = new ShopService();
        // Вывести полную информацию о заданном заказе.
        System.out.println(service.orderInfo(89001));
        // Вывести номера заказов, сумма которых не превосходит заданную, и количество различных товаров равно заданному.
        System.out.println(service.orderNumbers(20, 2));
        // Вывести номера заказов, содержащих заданный товар.
        System.out.println(service.hasProduct("Orange"));
        // Вывести номера заказов, не содержащих заданный товар и поступивших в течение текущего дня.
        LocalDate today = LocalDate.of(2022, 9, 20);
        System.out.println(service.hasntProductToday("Orange", today));
    }
}
