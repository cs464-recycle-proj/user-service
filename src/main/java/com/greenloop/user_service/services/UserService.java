package com.greenloop.user_service.services;

import com.greenloop.user_service.dtos.CreateUserRequest;
import com.greenloop.user_service.dtos.UpdateAvatarRequest;
import com.greenloop.user_service.dtos.UpdateUserRequest;
import com.greenloop.user_service.dtos.UserInterestsResponse;
import com.greenloop.user_service.dtos.UserResponse;
import com.greenloop.user_service.enums.Interest;
import com.greenloop.user_service.exceptions.UserNotFoundException;
import com.greenloop.user_service.models.User;
import com.greenloop.user_service.models.UserInterest;
import com.greenloop.user_service.repos.UserInterestRepository;
import com.greenloop.user_service.repos.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service layer for user profile and interest management.
 * <p>
 * Handles business logic for user CRUD operations, avatar updates,
 * interest tracking, and DTO mapping. Coordinates with UserRepository
 * and UserInterestRepository for database persistence.
 * </p>
 *
 * @see User
 * @see UserInterest
 * @see UserRepository
 * @see UserInterestRepository
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInterestRepository interestRepository;

    /**
     * Retrieves all user profiles from the database.
     *
     * @return list of all users as UserResponse DTOs
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id user's UUID
     * @return user profile as UserResponse DTO
     * @throws UserNotFoundException if no user exists with the given ID
     */
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " was not found."));
    }

    /**
     * Creates a new user with pre-authenticated credentials.
     * 
     * @param request   user profile information
     * @param userId    authenticated user ID from auth service
     * @param userEmail authenticated user email from auth service
     */
    public UserResponse createUser(CreateUserRequest request, UUID userId, String userEmail) {
        User user = User.builder()
                .id(userId)
                .email(userEmail)
                .username(request.getUsername())
                .birthDate(request.getBirthDate())
                .avatarUrl(request.getAvatarUrl())
                .bio(request.getBio())
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    /**
     * Updates an existing user's profile fields.
     * Only non-null fields in the request are updated.
     *
     * @param id      user's UUID
     * @param request partial update DTO containing modified fields
     * @return updated user profile as UserResponse DTO
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    // Update only non-null fields
                    if (request.getUsername() != null) {
                        user.setUsername(request.getUsername());
                    }
                    if (request.getBio() != null) {
                        user.setBio(request.getBio());
                    }
                    if (request.getBirthDate() != null) {
                        user.setBirthDate(request.getBirthDate());
                    }

                    return mapToResponse(userRepository.save(user));
                })
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " was not found."));
    }

    /**
     * Updates a user's avatar URL.
     *
     * @param id      user's UUID
     * @param request DTO containing new avatar URL
     * @return updated user profile as UserResponse DTO
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @Transactional
    public UserResponse updateAvatar(UUID id, UpdateAvatarRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setAvatarUrl(request.getAvatarUrl());
                    return mapToResponse(userRepository.save(user));
                })
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " was not found."));
    }

    /**
     * Deletes a user permanently from the database.
     *
     * @param id user's UUID
     * @throws UserNotFoundException if no user exists with the given ID
     */
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " was not found.");
        }
        userRepository.deleteById(id);
    }

    /**
     * Maps a User entity to a UserResponse DTO.
     *
     * @param user the User entity
     * @return UserResponse DTO with profile data
     */
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .birthDate(user.getBirthDate())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .joinedDate(user.getJoinedDate())
                .eventsAttended(user.getEventsAttended())
                .eventsRegistered(user.getEventsRegistered())
                .build();
    }

    /**
     * Retrieves all interests associated with a user.
     *
     * @param userId user's UUID
     * @return UserInterestsResponse DTO containing list of interest strings
     */
    public UserInterestsResponse getInterests(UUID userId) {
        List<UserInterest> userInterests = interestRepository.findByUserId(userId);
        return mapInterestsResponse(userInterests);
    }

    /**
     * Adds new interests to a user's profile.
     * Skips interests that already exist for the user.
     *
     * @param userId    user's UUID
     * @param interests list of interest strings (converted to Interest enum)
     * @return UserInterestsResponse DTO with added interests
     * @throws UserNotFoundException if no user exists with the given ID
     */
    public UserInterestsResponse addInterests(UUID userId, List<String> interests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " was not found."));

        List<UserInterest> userInterests = new ArrayList<>();
        for (String interestString : interests) {
            Interest interestEnum = Interest.valueOf(interestString.toUpperCase());
            boolean exists = interestRepository.existsByUserIdAndInterest(userId, interestEnum);

            if (!exists) {
                UserInterest ui = UserInterest.builder()
                        .user(user)
                        .interest(interestEnum)
                        .build();
                userInterests.add(ui);
            }
        }

        if (!userInterests.isEmpty()) {
            interestRepository.saveAll(userInterests);
        }
        return mapInterestsResponse(userInterests);
    }

    /**
     * Maps a list of UserInterest entities to a UserInterestsResponse DTO.
     *
     * @param userInterests list of UserInterest entities
     * @return UserInterestsResponse DTO with interest strings
     */
    private UserInterestsResponse mapInterestsResponse(List<UserInterest> userInterests) {
        List<String> interestStrings = userInterests.stream()
                .map(UserInterest::getInterest)
                .map(Object::toString)
                .toList();

        return UserInterestsResponse.builder()
                .interests(interestStrings)
                .build();
    }
}