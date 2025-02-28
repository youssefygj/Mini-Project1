package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Cart {
    private UUID id;
    private UUID userId;
    private List<Product> products=new ArrayList<>();

    public Cart(UUID userId, List<Product> products) {
        this.userId = userId;
        this.products = products;
    }
}
