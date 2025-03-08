package com.example.controller;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j //TODO log
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;
    private final ProductService productService;

    @PostMapping("/")
    public User addUser(@RequestBody User user) {

        return userService.addUser(user);
    }

    @GetMapping("/")
    public ArrayList<User> getUsers() {

        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {

        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {

        return userService.getOrdersByUserId(userId);
    }

    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {

        userService.addOrderToUser(userId);

        return "Order Added Successfully";
    }

    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {

        userService.removeOrderFromUser(userId, orderId);

        return "Order Removed Successfully";
    }

    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {

        userService.emptyCart(userId);

        return "Cart Emptied Successfully";
    }

    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {

        Product product = productService.getProductById(productId);
        Cart cart = cartService.getCartByUserId(userId);

        cartService.addProductToCart(cart.getId(), product);

        return "Product Added to Cart Successfully";
    }

    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {

        Product product = productService.getProductById(productId);
        User user = userService.getUserById(userId);
        cartService.deleteProductFromCart(user.getId(), product);

        return "Product Deleted from Cart Successfully";
    }

    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);

        return "User Deleted Successfully";
    }

}