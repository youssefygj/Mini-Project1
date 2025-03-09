package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class ProductService extends MainService<Product> {
    ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        try {
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product cannot be null");
            }
            if (product.getId() != null && this.productRepository.getProductById(product.getId()) != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Product already exists");
            }
            return this.productRepository.addProduct(product);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add product");
        }
    }

    public ArrayList<Product> getProducts() {
        try {
            return this.productRepository.getProducts();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get products");
        }
    }

    public Product getProductById(UUID productId){
        try {
            if (productId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product id cannot be null");
            }
            Product product = this.productRepository.getProductById(productId);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.valueOf(400), "Product not found");
            }
            return product;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get product by id");
        }
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        if (productId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product id cannot be null");
        }
        Product product = this.productRepository.getProductById(productId);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        try {
            return this.productRepository.updateProduct(productId, newName, newPrice);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update product");
        }
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        try {
            if (discount < 0 || discount > 100) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Discount must be between 0 and 100");
            }
            for (UUID productId : productIds) {
                Product product = this.productRepository.getProductById(productId);
                if (product == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id " + productId);
                }
            }
            this.productRepository.applyDiscount(discount, productIds);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to apply discount");
        }
    }

    public void deleteProductById(UUID productId) {
        try {
            if (productId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product id cannot be null");
            }
            Product product = this.productRepository.getProductById(productId);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
            }
            this.productRepository.deleteProductById(productId);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete product");
        }
    }
}
