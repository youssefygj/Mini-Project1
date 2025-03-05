package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {
        return this.productService.addProduct(product);
    }

    @GetMapping("/")
    public ArrayList<Product> getProducts() {
        return this.productService.getProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable UUID productId) {
        return this.productService.getProductById(productId);
    }

    @PutMapping("/update/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody Map<String,Object> body) {
        String newName = (body.containsKey("newName")) ? (String) body.get("newName") : null;
        if (newName != null && newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        double newPrice = 0;
        if (body.containsKey("newPrice")) {
            if(body.get("newPrice") instanceof Double) {
                newPrice = (double) body.get("newPrice");
            } else {
                throw new IllegalArgumentException("Price must be a valid number");
            }
        } else {
            throw new IllegalArgumentException("Price must be provided");
        }
        return this.productService.updateProduct(productId, newName, newPrice);
    }

    @PutMapping("/applyDiscount")
    public String applyDiscount(@RequestParam double discount,@RequestBody ArrayList<UUID> productIds) {
        this.productService.applyDiscount(discount, productIds);
        return "Discount applied successfully";
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable UUID productId) {
        this.productService.deleteProductById(productId);
        return "Product deleted successfully";
    }


}
