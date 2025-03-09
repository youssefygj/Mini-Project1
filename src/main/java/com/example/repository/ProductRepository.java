package com.example.repository;
import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {

    CartRepository cartRepository;

    public ProductRepository(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Value("${spring.application.productDataPath}")
    private String dataPath;

    @Override
    protected String getDataPath() {
        return dataPath;
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    public Product addProduct(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }
        save(product);
        return product;
    }

    public ArrayList<Product> getProducts() {
        return findAll();
    }

    public Product getProductById(UUID productId) {
        Product product = findAll().stream()
            .filter(p -> p.getId().equals(productId))
            .findFirst().orElse(null);
        return product;
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = findAll();
        ArrayList<Product> updatedProducts = new ArrayList<Product>(products.stream()
            .peek(p -> {
                if ( p.getId().equals(productId)) {
                    p.setName(newName);
                    p.setPrice(newPrice);
                }
            }).toList());
        Product updatedProduct = updatedProducts.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst().orElse(null);
        if (updatedProduct == null) {
            return null;
        }
        overrideData(updatedProducts);
        List<Cart> allCarts = this.cartRepository.getCarts();
        for (Cart cart : allCarts) {
            List<Product> cartProducts = cart.getProducts();
            boolean found = false;
            for (Product product : cartProducts) {
                if (productId.equals(product.getId())) {
                    product.setName(updatedProduct.getName());
                    product.setPrice(updatedProduct.getPrice());
                    found = true;
                    break;
                }
            }
            if (found) {
                this.cartRepository.updateProductsInCart(cart.getId(), cartProducts);
            }
        }
        return updatedProduct;
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        ArrayList<Product> products = findAll();
        ArrayList<Product> updatedProducts = new ArrayList<Product>(products.stream()
                .peek(p -> {
                    if (productIds.contains(p.getId())) {
                        p.setPrice(p.getPrice() * ((100 - discount)/100));
                    }
                }).toList());
        overrideData(updatedProducts);
        List<Cart> allCarts = this.cartRepository.getCarts();
        for (Cart cart : allCarts) {
            List<Product> cartProducts = cart.getProducts();
            boolean found = false;
            for (Product product : cartProducts) {
                if (productIds.contains(product.getId())) {
                    product.setPrice(product.getPrice() * ((100 - discount)/100));
                    found = true;
                    System.out.println("Found product: " + product);
                }
            }
            if (found) {
                this.cartRepository.updateProductsInCart(cart.getId(), cartProducts);
            }
        }
    }

    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();
        Product productTobeDeleted = this.getProductById(productId);
        if (productTobeDeleted == null) {
            return;
        }
        ArrayList<Product> updatedProducts = new ArrayList<Product>(products.stream()
                .filter(p -> !p.getId().equals(productId))
                .toList());
        overrideData(updatedProducts);
        List<Cart> allCarts = this.cartRepository.getCarts();
        for (Cart cart : allCarts) {
            List<Product> cartProducts = cart.getProducts();
            boolean found = false;
            for (Product product : cartProducts) {
                if (productId.equals(product.getId())) {
                    found = true;
                    break;
                }
            }
            if (found) {
                this.cartRepository.deleteProductFromCart(cart.getId(), productTobeDeleted);
            }
        }
    }

}
