package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Order {
    private UUID id;
    private UUID userId;
    private double totalPrice;
    private List<Product> products = new ArrayList<>();

    public Order(UUID userId){
        id = UUID.randomUUID();
        this.userId = userId;
    }

    public Order(UUID userId, List<Product> products){
        id = UUID.randomUUID();
        this.userId = userId;
        this.products = products;
        for (Product product : products) {
            this.totalPrice += product.getPrice();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order that = (Order) o;
        if (Double.compare(that.totalPrice, totalPrice) != 0 ||
                !Objects.equals(id, that.id) ||
                !Objects.equals(userId, that.userId) ||
                products.size() != that.products.size()) {
            return false;
        }

        for (int i = 0; i < products.size(); i++) {
            if (!products.get(i).equals(that.products.get(i))) {
                return false;
            }
        }

        return true;
    }

}
