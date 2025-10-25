package com.greenloop.user_service.controllers;

import com.greenloop.user_service.dtos.*;
import com.greenloop.user_service.models.User;
import com.greenloop.user_service.services.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RecommendationService recommendationService;

    public UserController(UserService userService, RecommendationService recommendationService) {
        this.userService = userService;
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public User createUser(@RequestBody UserDTO userDTO, @RequestHeader("X-User-ID") String userId, @RequestHeader("X-User-Email") String userEmail) {
        UUID userUuid = UUID.fromString(userId);
        return userService.createUser(userDTO, userUuid, userEmail);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/profile")
    public User getUserProfile(@RequestHeader("X-User-ID") String userId) {
        return userService.getUserById(UUID.fromString(userId));
    }

    @PutMapping
    public User updateUser(@RequestHeader("X-User-ID") String userId, @RequestBody User user) {
        return userService.updateUser(UUID.fromString(userId), user);
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader("X-User-ID") String userId) {
        userService.deleteUser(UUID.fromString(userId));
    }

    // ----- Recommendation -----
    @GetMapping("/recommendations")
    public List<EventRecommendationDTO> getRecommendations(@RequestHeader("X-User-ID") String userId, @RequestBody List<EventDTO> upcomingEvents) {
        return recommendationService.recommendEvents(UUID.fromString(userId), upcomingEvents);
    }

}