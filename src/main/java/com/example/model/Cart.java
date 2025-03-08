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
public class Cart {
    private UUID id;
    private UUID userId;
    private List<Product> products=new ArrayList<>();

    public Cart(UUID userId) {
        id = UUID.randomUUID();
        this.userId = userId;
    }

    public Cart(UUID userId, List<Product> products) {
        id = UUID.randomUUID();
        this.userId = userId;
        this.products = products;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Cart cart = (Cart) obj;

        if (!Objects.equals(id, cart.id) || !Objects.equals(userId, cart.userId)) {
            return false;
        }

        if (products.size() != cart.products.size()) {
            return false;
        }

        for (int i = 0; i < products.size(); i++) {
            if (!products.get(i).equals(cart.products.get(i))) {
                return false;
            }
        }

        return true;
    }
}
