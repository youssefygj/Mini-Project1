package com.example.controller;

import com.example.model.Order;
import com.example.service.MainService;
import com.example.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService _orderService;
    public OrderController(OrderService orderService) {
        _orderService = orderService;
    }

    @PostMapping("/")
    public void addOrder(@RequestBody Order order) {
        _orderService.addOrder(order);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable UUID orderId) {
        return _orderService.getOrderById(orderId);
    }

    @GetMapping("/")
    public ArrayList<Order> getOrders() {
        return _orderService.getOrders();
    }

    @DeleteMapping("/delete/{orderId}")
    public String deleteOrderById(@PathVariable UUID orderId) {
        try{
            _orderService.deleteOrderById(orderId);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return "Order not found";
        }
        return "Order deleted successfully";
    }
}
