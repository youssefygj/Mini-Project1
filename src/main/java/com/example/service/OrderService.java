package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class OrderService extends MainService<Order>{

    private final OrderRepository _orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        _orderRepository = orderRepository;
    }

    public void addOrder(Order order) {
        _orderRepository.addOrder(order);
    }

    public ArrayList<Order> getOrders() {
        return _orderRepository.getOrders();
    }

    public Order getOrderById(UUID id) {
        return _orderRepository.getOrderById(id);
    }

    public void deleteOrderById(UUID id) throws IllegalArgumentException {
        if (_orderRepository.getOrderById(id) == null) {
            throw new IllegalArgumentException("Invalid order id");
        }
        _orderRepository.deleteOrderById(id);
    }
}
