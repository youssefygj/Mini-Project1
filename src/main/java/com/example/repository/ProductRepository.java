package com.example.repository;
import com.example.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {

    public ProductRepository() {}

    @Override
    protected String getDataPath() {
        return "./data/products.txt";
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    public Product addProduct(Product product) {
        try {
            save(product);
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add product");
        }
    }

    public ArrayList<Product> getProducts() {
        try {
            return findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get products");
        }
    }

    public Product getProductById(UUID productId) {
        try {
            return findAll().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get product by id");
        }
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        try {
            ArrayList<Product> products = findAll();
            ArrayList<Product> updatedProducts = (ArrayList<Product>) products.stream()
                .peek(p -> {
                    if (p.getId().equals(productId)) {
                        p.setName(newName);
                        p.setPrice(newPrice);
                    }
                }).toList();
            overrideData(updatedProducts);
            return updatedProducts.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update product");
        }
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        try {
            ArrayList<Product> products = findAll();
            ArrayList<Product> updatedProducts = (ArrayList<Product>) products.stream()
                    .peek(p -> {
                        if (productIds.contains(p.getId())) {
                            p.setPrice(p.getPrice() * ((100 - discount)/100));
                        }
                    }).toList();
            overrideData(updatedProducts);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to apply discount");
        }
    }

    public void deleteProductById(UUID productId) {
        try {
            ArrayList<Product> products = findAll();
            ArrayList<Product> updatedProducts = (ArrayList<Product>) products.stream()
                .filter(p -> !p.getId().equals(productId))
                .toList();
            overrideData(updatedProducts);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete product");
        }
    }
}
