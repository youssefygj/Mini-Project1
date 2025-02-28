package com.example.service;

import com.example.model.Order;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends MainService<User>{
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user){
        userRepository.save(user);
        return user;
    }

    public ArrayList<User> getUsers(){
        return userRepository.getUsers();
    }

    public User getUserById(UUID userId){
        return userRepository.getUserById(userId);
    }

    public List<Order> getOrdersByUserId(UUID userId){
        return userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId){
       //until cart is done
    }

    public void emptyCart(UUID userId){
        //userRepository.getOrdersByUserId(userId).forEach(order -> order.getProducts().clear());
        //until cart is done
    }

    public void removeOrderFromUser(UUID userId, UUID orderId){
        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId){
        userRepository.deleteUserById(userId);
    }

}