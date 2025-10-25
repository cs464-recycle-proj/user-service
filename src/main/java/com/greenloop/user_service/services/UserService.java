package com.greenloop.user_service.services;

import com.greenloop.user_service.dtos.UserDTO;
import com.greenloop.user_service.exceptions.UserNotFoundException;
import com.greenloop.user_service.models.User;
import com.greenloop.user_service.repos.UserRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // get one user
    public User getUserById(UUID id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    // create new user
    public User createUser(UserDTO userDTO, UUID userId, String userEmail) {

        // create new user
        User user = new User();

        // set user details
        user.setId(userId);
        user.setEmail(userEmail);

        user.setName(userDTO.getName());
        user.setAvatarUrl(userDTO.getAvatarUrl());
        user.setBio(userDTO.getBio());
        user.setBday(userDTO.getBday());

        return userRepo.save(user);
    }

    // Update user
    public User updateUser(UUID id, User newUser) {
        return userRepo.findById(id).map(u -> {
            u.setName(newUser.getName());
            u.setEmail(newUser.getEmail());
            u.setAvatarUrl(newUser.getAvatarUrl());
            u.setBio(newUser.getBio());
            u.setBday(newUser.getBday());
            return userRepo.save(u);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

    // Delete user
    public void deleteUser(UUID id) {
        userRepo.deleteById(id);
    }

}