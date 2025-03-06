package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {
    CartRepository cartRepository;
    @Autowired


    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart addCart(Cart cart) {
        if(cart == null) {
            throw new ResourceNotFoundException("Cart is null");
        }
        return cartRepository.addCart(cart);
    }

    
    public ArrayList<Cart> getCarts() {
        ArrayList<Cart> carts = cartRepository.getCarts();
        if(carts == null) {
            throw new ResourceNotFoundException("No Carts found");
        }
        return carts;
    }

   
    public Cart getCartById(UUID cartId) {
        if (cartId == null) {
            throw new ResourceNotFoundException("Cart ID is null");
        }
        Cart cart = cartRepository.getCartById(cartId);
        if(cart == null) {
            throw new ResourceNotFoundException("No Carts found");
        }
        return cart;
    }

   
    public Cart getCartByUserId(UUID userId) {
        if (userId == null) {
            throw new ResourceNotFoundException("User ID is null");
        }
        Cart cart = cartRepository.getCartByUserId(userId);
        if(cart == null) {
            throw new ResourceNotFoundException("No Carts found, Likely User doesn't exist");
        }
        return cart;
    }

   
    public void addProductToCart(UUID cartId, Product product) {
        if (cartId == null) {
            throw new ResourceNotFoundException("Cart ID is null");
        } else if(product == null) {
            throw new ResourceNotFoundException("Product is null");
        }
        cartRepository.addProductToCart(cartId, product);
    }

    
    public void deleteProductFromCart(UUID cartId, Product product) {
        if (cartId == null) {
            throw new ResourceNotFoundException("Cart ID is null");
        }else if(product == null) {
            throw new ResourceNotFoundException("Product is null");
        }
        cartRepository.deleteProductFromCart(cartId, product);
    }

    public void deleteCartById(UUID cartId) {
        if(cartId == null) {
            throw new ResourceNotFoundException("Cart ID is null");
        }
        cartRepository.deleteCartById(cartId);
    }
}
