package com.greenloop.user_service.services;

import com.greenloop.user_service.dtos.UserActionDTO;
import com.greenloop.user_service.enums.UserAction;
import com.greenloop.user_service.models.User;
import com.greenloop.user_service.models.UserInterest;
import com.greenloop.user_service.models.UserActionRecord;
import com.greenloop.user_service.repos.UserActionRecordRepository;
import com.greenloop.user_service.repos.UserInterestRepository;
import com.greenloop.user_service.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final UserInterestRepository interestRepo;
    private final UserActionRecordRepository historyRepo;

    public UserService(UserRepository userRepo, UserInterestRepository interestRepo,
            UserActionRecordRepository historyRepo) {
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
        if (user == null){
            return null;
        }
        UserInterest ui = new UserInterest(user, interest);
        return interestRepo.save(ui);
    }

    // Event History
    public List<UserActionRecord> getEventHistory(UUID userId) {
        return historyRepo.findByUserId(userId);
    }

    public UserActionRecord addUserActionRecord(UUID userId, UserActionDTO userActionDTO ) {
        User user = getUserById(userId);
        if (user == null){
            return null;
        }
           
        UserActionRecord hist = new UserActionRecord(user, userActionDTO.getEventId(), userActionDTO.getAction(), userActionDTO.getRating());
        return historyRepo.save(hist);
    }
}