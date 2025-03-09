package com.example.service;

import com.example.model.Order;
import com.example.model.User;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class OrderService extends MainService<Order>{

    private final OrderRepository _orderRepository;
    private final UserRepository _userRepository;
    private final ProductRepository _productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this._orderRepository = orderRepository;
        this._userRepository = userRepository;
        this._productRepository = productRepository;
    }

    public void addOrder(Order order) throws IllegalArgumentException  {

        if (order == null){
            throw new IllegalArgumentException("Order is null");
        }

        if(order.getProducts() == null){
            throw new IllegalArgumentException("Products inside order is null");
        }

        if (order.getUserId() == null){
            throw new IllegalArgumentException("User associated with order is null");
        }

        if(order.getId() == null){
            throw new IllegalArgumentException("Order id is null");
        }

        if (_orderRepository.getOrderById(order.getId()) != null){
            throw new IllegalArgumentException("Order already exists");
        }

        order.getProducts().forEach(p -> { if(_productRepository.getProductById(p.getId()) == null){
            throw new IllegalArgumentException("Product does not exist");
        }});

        User associatedUser = _userRepository.getUserById(order.getUserId());

        if(associatedUser == null){
            throw new IllegalArgumentException("User does not exist");
        }

        _userRepository.addOrderToUser(order.getUserId(), order);
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
        UUID userOwner = _orderRepository.getOrderById(id).getUserId();
        _orderRepository.deleteOrderById(id);
        if(_userRepository.getUserById(userOwner) != null){
            _userRepository.removeOrderFromUser(userOwner,id);
        }
    }
}
