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
        this._orderRepository = orderRepository;
    }

    public void addOrder(Order order) throws IllegalArgumentException  {
        if (order == null){
            throw new IllegalArgumentException("Order is null");
        }

        if (_orderRepository.getOrderById(order.getId()) != null){
            throw new IllegalArgumentException("Order already exists");
        }

        _orderRepository.addOrder(order);
    }

    public ArrayList<Order> getOrders() {
        return _orderRepository.getOrders();
    }

    public Order getOrderById(UUID id) throws IllegalArgumentException  {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        if (_orderRepository.getOrderById(id) == null) {
            throw new IllegalArgumentException("Invalid order id");
        }

        return _orderRepository.getOrderById(id);
    }

    public void deleteOrderById(UUID id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        if (_orderRepository.getOrderById(id) == null) {
            throw new IllegalArgumentException("Invalid order id");
        }

        _orderRepository.deleteOrderById(id);
    }
}
