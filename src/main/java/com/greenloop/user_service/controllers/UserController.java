package com.greenloop.user_service.controllers;

import com.greenloop.user_service.dtos.EventDTO;
import com.greenloop.user_service.dtos.EventRecommendationDTO;
import com.greenloop.user_service.dtos.UserActionDTO;
import com.greenloop.user_service.models.User;
import com.greenloop.user_service.models.UserActionRecord;
import com.greenloop.user_service.models.UserInterest;
import com.greenloop.user_service.services.RecommendationService;
import com.greenloop.user_service.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final RecommendationService recommendationService;

    public UserController(UserService userService, RecommendationService recommendationService) {
        this.userService = userService;
        this.recommendationService = recommendationService;
    }

    // ----- User CRUD -----
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    // ----- Interests -----
    @GetMapping("/users/{id}/interests")
    public List<UserInterest> getInterests(@PathVariable UUID id) {
        return userService.getInterests(id);
    }

    @PostMapping("/users/{id}/interests")
    public UserInterest addInterest(@PathVariable UUID id, @RequestBody String interest) {
        return userService.addInterest(id, interest);
    }

    // ----- Event History -----
    @GetMapping("/users/{id}/events/history")
    public List<UserActionRecord> getEventHistory(@PathVariable UUID id) {
        return userService.getEventHistory(id);
    }

    @PostMapping("/users/{id}/events/history")
    public UserActionRecord addEventHistory(@PathVariable UUID id, @RequestBody UserActionDTO userActionDTO) {
        return userService.addUserActionRecord(id, userActionDTO);
    }

    // ----- Recommendation -----
    @GetMapping("/users/{id}/recommendations")
    public List<EventRecommendationDTO> getRecommendations(@PathVariable UUID id, @RequestBody List<EventDTO> upcomingEvents) {
        return recommendationService.recommendEvents(id, upcomingEvents);
    }

}