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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInterestRepository interestRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

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

    @Transactional
    public UserResponse updateAvatar(UUID id, UpdateAvatarRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setAvatarUrl(request.getAvatarUrl());
                    return mapToResponse(userRepository.save(user));
                })
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " was not found."));
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " was not found.");
        }
        userRepository.deleteById(id);
    }

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

    public UserInterestsResponse getInterests(UUID userId) {
        List<UserInterest> userInterests = interestRepository.findByUserId(userId);
        return mapInterestsResponse(userInterests);
    }

    public UserInterestsResponse addInterests(UUID userId, List<String> interests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " was not found."));

        List<UserInterest> userInterests = new ArrayList<>();
        for (String interest : interests) {
            boolean exists = interestRepository.existsByUserIdAndInterest(userId, interest);
            if (!exists) {
                UserInterest ui = UserInterest.builder()
                        .user(user)
                        .interest(Interest.valueOf(interest.toUpperCase()))
                        .build();
                userInterests.add(ui);
            }
        }

        if (!userInterests.isEmpty()) {
            interestRepository.saveAll(userInterests);
        }
        return mapInterestsResponse(userInterests);
    }

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