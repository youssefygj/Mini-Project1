package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService extends MainService<User> {
    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderService orderService;

    public User addUser(User user) {
        userRepository.save(user);
        return user;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = userRepository.getUsers();
        if (users == null) throw new ResourceNotFoundException("No users found");

        return users;
    }

    public User getUserById(UUID userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) throw new ResourceNotFoundException("User not found");

        return user;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        List<Order> orders = userRepository.getOrdersByUserId(userId);
        if (orders == null) throw new ResourceNotFoundException("Orders not found, likely User doesn't exist");

        return orders;
    }

    public void addOrderToUser(UUID userId) {
        if (userId == null) throw new ResourceNotFoundException("User id is null");

        Order newOrder = new Order();
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) throw new ResourceNotFoundException("Cart not found, likely User doesn't exist");

        for(Product product : cart.getProducts()){
            newOrder.setTotalPrice(newOrder.getTotalPrice() + product.getPrice());
            newOrder.getProducts().add(product);
            cartService.deleteProductFromCart(cart.getId(), product);
        }

        userRepository.addOrderToUser(userId, newOrder);
        orderService.addOrder(newOrder);
    }

    public void emptyCart(UUID userId) {
        if (userId == null) throw new ResourceNotFoundException("User id is null");

        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) throw new ResourceNotFoundException("Cart not found, likely User doesn't exist");

        List<Product> products = cart.getProducts();
        for (Product product : products) {
            cartService.deleteProductFromCart(cart.getId(), product);
        }
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        if (userId == null) throw new ResourceNotFoundException("order id is null");

        if (orderId == null) throw new ResourceNotFoundException("User id is null");

        if (userRepository.getUserById(userId) == null) throw new ResourceNotFoundException("User not found");

        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId) {
        if (userId == null) throw new ResourceNotFoundException("User id is null");

        if (userRepository.getUserById(userId) == null) throw new ResourceNotFoundException("User not found");

        userRepository.deleteUserById(userId);
    }

}