package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
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
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final OrderService orderService;

    public User addUser(User user) {
        log.info("Adding user: {}", user);

        if(user == null || user.getId() == null) throw new IllegalArgumentException("User is null");
        if(userRepository.getUserById(user.getId()) != null) throw new IllegalArgumentException("User already exists");

        userRepository.save(user);

        log.info("user added successfully");
        return user;
    }

    public ArrayList<User> getUsers() {
        log.info("Getting users");

        ArrayList<User> users = userRepository.getUsers();
        if (users == null) throw new ResourceNotFoundException("No users found");

        log.info("Found {} users", users.size());
        return users;
    }

    public User getUserById(UUID userId) {
        log.info("Getting user by id: {}", userId);

        User user = userRepository.getUserById(userId);
        if (user == null) throw new ResourceNotFoundException("User not found");

        log.info("Found user: {}", user);
        return user;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        log.info("getting orders by user id: {}", userId);

        List<Order> orders = userRepository.getOrdersByUserId(userId);
        if (orders == null) throw new ResourceNotFoundException("Orders not found, likely User doesn't exist");

        log.info("Found {} orders", orders.size());
        return orders;
    }

    public void addOrderToUser(UUID userId) {
        log.info("Adding order to user: {}", userId);

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

        log.info("order added successfully");
    }

    public void emptyCart(UUID userId) {
        log.info("emptying user's cart with id {}", userId);

        if (userId == null) throw new ResourceNotFoundException("User id is null");

        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart != null)
        {
            List<Product> products = cart.getProducts();
            for (Product product : products) {
                cartService.deleteProductFromCart(cart.getId(), product);
            }
        }

        log.info("Cart emptied successfully");
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        log.info("Removing order with id {} from user with id {}", userId, orderId);

        if (userId == null) throw new ResourceNotFoundException("order id is null");
        if (orderId == null) throw new ResourceNotFoundException("User id is null");

        User user = userRepository.getUserById(userId);
        if (user == null) throw new ResourceNotFoundException("User not found");

        Cart cart = cartRepository.getCartByUserId(userId);
        Order userOrder = orderService.getOrderById(orderId);
        if(userOrder == null) throw new ResourceNotFoundException("Order not found");
        if(cart != null)
            for (Product product : userOrder.getProducts()) {
                cartService.deleteProductFromCart(cart.getId(), product);
            }

        orderService.deleteOrderById(orderId);
        userRepository.removeOrderFromUser(userId, orderId);

        log.info("Removed order successfully");
    }

    public void deleteUserById(UUID userId) {
        log.info("starting deleting by id");

        if (userId == null) throw new ResourceNotFoundException("User id is null");

        log.info("getting user from DB");
        User user = userRepository.getUserById(userId);
        if (user == null) throw new ResourceNotFoundException("User not found");
        log.info("getting cart from DB");
        Cart cart = cartRepository.getCartByUserId(userId);
        if(cart != null)
            cartRepository.deleteCartById(cart.getId());
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