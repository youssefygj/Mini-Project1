package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService extends MainService<User> {
    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderService orderService;

    public User addUser(User user) {
        if(user == null || user.getId() == null) throw new IllegalArgumentException("User is null");
        if(userRepository.getUserById(user.getId()) != null) throw new IllegalArgumentException("User already exists");

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
        if(userRepository.getUserById(userId) == null) throw new ResourceNotFoundException("User not found");

        Order newOrder = new Order();

        newOrder.setId(UUID.randomUUID());

        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) throw new ResourceNotFoundException("Cart not found, likely User doesn't exist");
        if(cart.getProducts().isEmpty()) throw new ResourceNotFoundException("Cart is empty");

        for(Product product : cart.getProducts()){
            newOrder.setTotalPrice(newOrder.getTotalPrice() + product.getPrice());
            newOrder.getProducts().add(product);
            cartService.deleteProductFromCart(cart.getId(), product);
        }

        newOrder.setUserId(userId);
        orderService.addOrder(newOrder);
        userRepository.addOrderToUser(userId, newOrder);
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

        User user = userRepository.getUserById(userId);
        if (user == null) throw new ResourceNotFoundException("User not found");

        Cart cart = cartService.getCartByUserId(userId);
        Order userOrder = orderService.getOrderById(orderId);
        if(userOrder == null) throw new ResourceNotFoundException("Order not found");
        if(cart != null)
            for (Product product : userOrder.getProducts()) {
                cartService.deleteProductFromCart(cart.getId(), product);
            }

        orderService.deleteOrderById(orderId);
        userRepository.removeOrderFromUser(userId, orderId);

    }

    public void deleteUserById(UUID userId) {
        log.info("starting deleting by id");
        if (userId == null) throw new ResourceNotFoundException("User id is null");

        log.info("getting user from DB");
        User user = userRepository.getUserById(userId);
        if (user == null) throw new ResourceNotFoundException("User not found");
        log.info("getting cart from DB");
        Cart cart = cartService.getCartByUserId(userId);
        if(cart != null)
            cartService.deleteCartById(cart.getId());
        log.info("getting orders from DB");
        List<Order> orders = userRepository.getOrdersByUserId(userId);
        if(!orders.isEmpty()) {
            for (Order order : orders) {
                orderService.deleteOrderById(order.getId());
            }
        }

        userRepository.deleteUserById(userId);
        log.info("user deleted from DB");
    }

}