package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Autowired


    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Cart addCart(Cart cart) {
        if(cart == null) {
            throw new ResourceNotFoundException("Cart is null");
        }else if(cartRepository.getCartByUserId(cart.getUserId()) != null) {
            throw new DuplicateKeyException("Cart already exists");
        } else if (userRepository.getUserById(cart.getUserId()) == null) {
            throw new ResourceNotFoundException("User not found");
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
//        if(cart == null) {
//            throw new ResourceNotFoundException("No Carts found, Likely User doesn't exist");
//        }
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
        } else if (!cartRepository.getCartById(cartId).getProducts().contains(product)) {
            throw new ResourceNotFoundException("Product not in the cart already");
        }
        cartRepository.deleteProductFromCart(cartId, product);
    }

    public void deleteCartById(UUID cartId) {
        if(cartId == null) {
            throw new ResourceNotFoundException("Cart ID is null");
        }else if(cartRepository.getCartById(cartId) == null) {
            throw new ResourceNotFoundException("Cart not found");
        }
        cartRepository.deleteCartById(cartId);
    }
}