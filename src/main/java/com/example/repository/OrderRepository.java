package com.example.repository;

import com.example.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("rawtypes")

public class OrderRepository extends MainRepository<Order>{

    @Value("${spring.application.cartDataPath}")
    private String dataPath;

    @Override
    protected String getDataPath() {
        return dataPath;
    }

    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class;
    }

    public void addOrder(Order order) {
        save(order);
    }

    public ArrayList<Order> getOrders() {
        return findAll();
    }

    public Order getOrderById(UUID orderId){
        ArrayList<Order> orders = getOrders();
        return orders.stream().filter(order -> order.getId().equals(orderId)).findFirst().orElse(null);
    }

    public void deleteOrderById(UUID orderId) {
        ArrayList<Order> orders = getOrders();
        List<Order> filteredOrder= orders.stream().filter(order -> ! order.getId().equals(orderId)).collect(Collectors.toList());
        overrideData((ArrayList<Order>) filteredOrder);
    }
}
