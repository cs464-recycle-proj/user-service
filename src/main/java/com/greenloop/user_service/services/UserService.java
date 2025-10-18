package com.greenloop.user_service.services;

import com.greenloop.user_service.models.User;
import com.greenloop.user_service.models.UserEventHistory;
import com.greenloop.user_service.models.UserInterest;
import com.greenloop.user_service.repos.UserEventHistoryRepository;
import com.greenloop.user_service.repos.UserInterestRepository;
import com.greenloop.user_service.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final UserInterestRepository interestRepo;
    private final UserEventHistoryRepository historyRepo;

    public UserService(UserRepository userRepo, UserInterestRepository interestRepo,
            UserEventHistoryRepository historyRepo) {
        this.userRepo = userRepo;
        this.interestRepo = interestRepo;
        this.historyRepo = historyRepo;
    }

    // User CRUD
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(UUID id) {
        return userRepo.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public User updateUser(UUID id, User newUser) {
        return userRepo.findById(id).map(u -> {
            u.setUsername(newUser.getUsername());
            u.setEmail(newUser.getEmail());
            u.setFullName(newUser.getFullName());
            u.setProfileImg(newUser.getProfileImg());
            u.setBio(newUser.getBio());
            u.setBday(newUser.getBday());
            return userRepo.save(u);
        }).orElse(null);
    }

    public void deleteUser(UUID id) {
        userRepo.deleteById(id);
    }

    // Interests
    public List<UserInterest> getInterests(UUID userId) {
        return interestRepo.findByUserId(userId);
    }

    public UserInterest addInterest(UUID userId, String interest) {
        User user = getUserById(userId);
        if (user == null)
            return null;
        UserInterest ui = new UserInterest(user, interest);
        return interestRepo.save(ui);
    }

    // Event History
    public List<UserEventHistory> getEventHistory(UUID userId) {
        return historyRepo.findByUserId(userId);
    }

    public UserEventHistory addEventHistory(UUID userId, UUID eventId, String action, Integer rating) {
        User user = getUserById(userId);
        if (user == null)
            return null;
        UserEventHistory hist = new UserEventHistory(user, eventId, action, rating);
        return historyRepo.save(hist);
    }

    // Recommendation: simple rule-based (returns event IDs of interest categories)
    public List<UUID> recommendEvents(UUID userId, List<UUID> allEventIds) {
        List<UserInterest> interests = getInterests(userId);
        // Placeholder: for now, just return first N events (mock)
        return allEventIds.stream().limit(5).collect(Collectors.toList());
    }
}