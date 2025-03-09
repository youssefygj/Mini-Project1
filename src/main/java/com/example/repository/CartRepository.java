package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        this.save(cart);
        return cart;
    }

    public ArrayList<Cart> getCarts() {
        return this.findAll();
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
        ArrayList<Cart> carts = this.findAll();
        Cart cart = Objects.requireNonNull(carts.stream()
                .filter(c -> c.getId().equals(cartId))
                .findFirst().orElse(null));
        cart.getProducts().add(product);

        this.overrideData(carts);
    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = this.findAll();
        Cart cart = Objects.requireNonNull(carts.stream()
                .filter(c -> c.getId().equals(cartId))
                .findFirst().orElse(null));
        cart.getProducts().remove(product);

        this.overrideData(carts);
    }

    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = this.findAll();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        this.overrideData(carts);
    }

    public void updateProductsInCart(UUID cartId, List<Product> products) {
        ArrayList<Cart> carts = this.findAll();
        ArrayList<Cart> updatedCarts = new ArrayList<Cart>(carts.stream()
                .peek(c -> {
                    if ( c.getId().equals(cartId)) {
                        c.setProducts(products);
                    }
                }).toList());
        this.overrideData(updatedCarts);
    }

}
