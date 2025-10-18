package com.greenloop.user_service.controllers;


import com.greenloop.user_service.models.User;
import com.greenloop.user_service.models.UserEventHistory;
import com.greenloop.user_service.models.UserInterest;
import com.greenloop.user_service.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService service;
    public UserController(UserService service) { this.service = service; }

    // ----- User CRUD -----
    @PostMapping("/users")
    public User createUser(@RequestBody User user) { return service.createUser(user); }

    @GetMapping("/users")
    public List<User> getAllUsers() { return service.getAllUsers(); }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable UUID id) { return service.getUserById(id); }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) { return service.updateUser(id, user); }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable UUID id) { service.deleteUser(id); }

    // ----- Interests -----
    @GetMapping("/users/{id}/interests")
    public List<UserInterest> getInterests(@PathVariable UUID id) { return service.getInterests(id); }

    @PostMapping("/users/{id}/interests")
    public UserInterest addInterest(@PathVariable UUID id, @RequestParam String interest) {
        return service.addInterest(id, interest);
    }

    // ----- Event History -----
    @GetMapping("/users/{id}/events/history")
    public List<UserEventHistory> getEventHistory(@PathVariable UUID id) { return service.getEventHistory(id); }

    @PostMapping("/users/{id}/events/history")
    public UserEventHistory addEventHistory(@PathVariable UUID id,
                                            @RequestParam UUID eventId,
                                            @RequestParam String action,
                                            @RequestParam(required = false) Integer rating) {
        return service.addEventHistory(id, eventId, action, rating);
    }

    // ----- Recommendation -----
    @GetMapping("/users/{id}/recommendations")
    public List<UUID> getRecommendations(@PathVariable UUID id, @RequestParam List<UUID> allEventIds) {
        return service.recommendEvents(id, allEventIds);
    }
}