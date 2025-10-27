package com.greenloop.user_service.controllers;

import com.greenloop.user_service.dtos.*;
import com.greenloop.user_service.services.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    // private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request,
            @RequestHeader("X-User-ID") String userId,
            @RequestHeader("X-User-Email") String userEmail) {

        UserResponse response = userService.createUser(request, parseUserId(userId), userEmail);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(
            @RequestHeader("X-User-ID") String userId) {

        UserResponse response = userService.getUserById(parseUserId(userId));
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", response));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserProfile(
            @RequestHeader("X-User-ID") String userId,
            @Valid @RequestBody UpdateUserRequest request) {

        UserResponse response = userService.updateUser(parseUserId(userId), request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", response));
    }

    @PatchMapping("/avatar")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserAvatar(
            @RequestHeader("X-User-ID") String userId,
            @Valid @RequestBody UpdateAvatarRequest request) {

        UserResponse response = userService.updateAvatar(parseUserId(userId), request);
        return ResponseEntity.ok(ApiResponse.success("Avatar updated successfully", response));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-User-ID") String userId) {
        userService.deleteUser(parseUserId(userId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/interests")
    public ResponseEntity<ApiResponse<UserInterestsResponse>> getInterests(@RequestHeader("X-User-ID") String userId) {
        UserInterestsResponse response = userService.getInterests(parseUserId(userId));
        return ResponseEntity.ok(ApiResponse.success("User interests retrieved successfully", response));
    }

    @PostMapping("/interests")
    public ResponseEntity<ApiResponse<UserInterestsResponse>> addInterests(@RequestHeader("X-User-ID") String userId,
            @RequestBody List<String> interests) {
        UserInterestsResponse response = userService.addInterests(parseUserId(userId), interests);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User interests retrieved successfully", response));
    }

    /**
     * Converts user ID header to UUID with proper error handling.
     */
    private UUID parseUserId(String userId) {
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user ID format: " + userId);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> checkHealth() {
        Map<String, String> response = Collections.singletonMap("status", "User Service is Up and Running!");
        return ResponseEntity.ok(response);
    }

    // ----- Recommendations -----
    // @GetMapping("/recommendations")
    // public ResponseEntity<ApiResponse<List<EventRecommendationDTO>>>
    // getRecommendations(
    // @RequestHeader("X-User-ID") String userId,
    // @RequestBody List<EventDTO> upcomingEvents) {
    //
    // List<EventRecommendationDTO> recommendations =
    // recommendationService.recommendEvents(parseUserId(userId), upcomingEvents);
    // return ResponseEntity.ok(ApiResponse.success("Recommendations retrieved",
    // recommendations));
    // }
}