package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository extends MainRepository<User> {
    @Value("${spring.application.userDataPath}")
    private String userDataPath;

    public UserRepository() {
        //Empty Constructor
    }

    @Override
    protected String getDataPath() {
        return userDataPath;
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }

    public ArrayList<User> getUsers() {
        return findAll();
    }

    public User getUserById(UUID userId) {
        return getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public User addUser(User user) {
        save(user);
        return user;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);

        return user.getOrders();
    }

    public void addOrderToUser(UUID userId, Order order) {
        User user = getUserById(userId);

        user.getOrders().add(order);
        save(user);
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        ArrayList<User> users = getUsers();
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);

        user.getOrders().removeIf(order -> order.getId().equals(orderId));
        overrideData(users);
    }

    public void deleteUserById(UUID userId) {
        User user = getUserById(userId);

        getUsers().remove(user);
        overrideData(getUsers());
    }
}
