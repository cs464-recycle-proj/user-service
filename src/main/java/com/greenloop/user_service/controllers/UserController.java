package com.greenloop.user_service.controllers;

import com.greenloop.user_service.dtos.*;
import com.greenloop.user_service.services.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST controller for user profile and interest management.
 * <p>
 * Handles user CRUD operations, profile updates, avatar changes, and interest
 * tracking.
 * All endpoints expect authenticated headers (X-User-ID, X-User-Email) injected
 * by the gateway.
 * </p>
 *
 * <p>
 * <strong>Base Path:</strong> /api/users
 * </p>
 *
 * @see UserService
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    // private final RecommendationService recommendationService;

    /**
     * Creates a new user profile.
     *
     * @param request   user profile details (username, birth date, avatar, bio)
     * @param userId    authenticated user ID from gateway (X-User-ID header)
     * @param userEmail authenticated user email from gateway (X-User-Email header)
     * @return newly created user profile wrapped in ApiResponse
     */
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

    /**
     * Retrieves all user profiles.
     *
     * @return list of all users wrapped in ApiResponse
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
    }

    /**
     * Retrieves the authenticated user's profile.
     *
     * @param userId authenticated user ID from gateway (X-User-ID header)
     * @return user profile wrapped in ApiResponse
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(
            @RequestHeader("X-User-ID") String userId) {

        UserResponse response = userService.getUserById(parseUserId(userId));
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", response));
    }

    /**
     * Updates the authenticated user's profile (username, bio, birth date).
     *
     * @param userId  authenticated user ID from gateway (X-User-ID header)
     * @param request partial update with non-null fields to modify
     * @return updated user profile wrapped in ApiResponse
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserProfile(
            @RequestHeader("X-User-ID") String userId,
            @Valid @RequestBody UpdateUserRequest request) {

        UserResponse response = userService.updateUser(parseUserId(userId), request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", response));
    }

    /**
     * Updates the authenticated user's avatar URL.
     *
     * @param userId  authenticated user ID from gateway (X-User-ID header)
     * @param request new avatar URL
     * @return updated user profile wrapped in ApiResponse
     */
    @PatchMapping("/avatar")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserAvatar(
            @RequestHeader("X-User-ID") String userId,
            @Valid @RequestBody UpdateAvatarRequest request) {

        UserResponse response = userService.updateAvatar(parseUserId(userId), request);
        return ResponseEntity.ok(ApiResponse.success("Avatar updated successfully", response));
    }

    /**
     * Deletes the authenticated user's profile permanently.
     *
     * @param userId authenticated user ID from gateway (X-User-ID header)
     * @return 204 No Content on successful deletion
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-User-ID") String userId) {
        userService.deleteUser(parseUserId(userId));
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the user's tracked interests.
     *
     * @param userId authenticated user ID from gateway (X-User-ID header)
     * @return list of user interests wrapped in ApiResponse
     */
    @GetMapping("/interests")
    public ResponseEntity<ApiResponse<UserInterestsResponse>> getInterests(@RequestHeader("X-User-ID") String userId) {
        UserInterestsResponse response = userService.getInterests(parseUserId(userId));
        return ResponseEntity.ok(ApiResponse.success("User interests retrieved successfully", response));
    }

    /**
     * Adds new interests to the user's profile.
     *
     * @param userId    authenticated user ID from gateway (X-User-ID header)
     * @param interests list of interest strings (e.g., ["RECYCLING",
     *                  "TREE_PLANTING"])
     * @return updated user interests wrapped in ApiResponse
     */
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
}