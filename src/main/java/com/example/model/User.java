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
public class User {
    private UUID id;
    private String name;
    private List<Order> orders = new ArrayList<>();

    public User(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public User(UUID id,String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;

        if (!Objects.equals(id, user.id) || !Objects.equals(name, user.name)) {
            return false;
        }

        if (orders.size() != user.orders.size()) {
            return false;
        }

        for (int i = 0; i < orders.size(); i++) {
            if (!orders.get(i).equals(user.orders.get(i))) {
                return false;
            }
        }

        return true;
    }
}