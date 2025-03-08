package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

@Component
public class Product {
    private UUID id;
    private String name;
    private double price;

    public Product(UUID id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public Product(String name, double price) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
    }

    public Product() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Double.compare(product.price, price) == 0 &&
                Objects.equals(id, product.id) &&
                Objects.equals(name, product.name);
    }
}
