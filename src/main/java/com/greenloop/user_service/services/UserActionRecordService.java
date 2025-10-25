package com.greenloop.user_service.services;

import com.greenloop.user_service.exceptions.UserNotFoundException;
import com.greenloop.user_service.models.User;
import com.greenloop.user_service.models.UserActionRecord;
import com.greenloop.user_service.repos.UserActionRecordRepository;
import com.greenloop.user_service.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserActionRecordService {

    private final UserRepository userRepo;
    private final UserActionRecordRepository actionRepo;

    public UserActionRecordService(UserRepository userRepo, UserActionRecordRepository actionRepo) {
        this.userRepo = userRepo;
        this.actionRepo = actionRepo;
    }

    public List<UserActionRecord> getUserActionRecords(UUID userId) {
        return actionRepo.findByUserId(userId);
    }

    public UserActionRecord addUserActionRecord(UUID userId, UserActionRecord actionRecord) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        // connect both sides
        actionRecord.setUser(user);
        user.getActionHistory().add(actionRecord);

        userRepo.save(user); // cascades to save actionRecord
        return actionRecord;
    }

}