package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart>{

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json";
    }

    @Override
    protected Class getArrayType() {
        return Cart[].class;
    }

    public CartRepository() {}


    public Cart addCart(Cart cart) {
        try {
            objectMapper.writeValue(new File(getDataPath()), cart);
            return cart;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }


    public ArrayList<Cart> getCarts() {
        try {
            return objectMapper.readValue(new File(getDataPath()), new TypeReference<ArrayList<Cart>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }


    public Cart getCartById(UUID cartId) {
        try {
            return this.findAll().stream()
                    .filter(cart ->  cart.getId().equals(cartId))
                    .findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to read carts.json");
        }
    }


    public Cart getCartByUserId(UUID userId) {
        try {
            return this.findAll().stream()
                    .filter(cart ->  cart.getUserId().equals(userId))
                    .findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to read carts.json");
        }
    }


    public void addProductToCart(UUID cartId, Product product) {
        Objects.requireNonNull(this.findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst().orElse(null)).getProducts().add(product);
    }


    public void deleteProductFromCart(UUID cartId, Product product) {
        Objects.requireNonNull(this.findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst().orElse(null)).getProducts().remove(product);
    }


    public void deleteCartById(UUID cartId) {
        this.findAll().removeIf(cart -> cart.getId().equals(cartId));
    }

}
