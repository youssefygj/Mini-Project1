package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {
    CartService cartService;
    @Autowired

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/")
    public Cart addCart(@RequestBody Cart cart) {
        return cartService.addCart(cart);
    }


    @GetMapping("/")
    public ArrayList<Cart> getCarts() {
        return cartService.getCarts();
    }


    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable UUID cartId) {
        return cartService.getCartById(cartId);
    }


    @PutMapping("/addProduct/{cartId}")
    public String addProductToCart(@PathVariable UUID cartId, @RequestBody Product product) {
        try {
            cartService.addProductToCart(cartId, product);
            return "Product added to cart successfully";
        } catch (Exception e) {
            return "Cart or Product not found";
        }
    }


    @DeleteMapping("/delete/{cartId}")
    public String deleteCartById(@PathVariable UUID cartId) {
        try {
            cartService.deleteCartById(cartId);
            return "Cart deleted successfully";
        } catch (Exception e) {
            return "Cart not found";
        }
    }


}
