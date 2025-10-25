package com.greenloop.user_service.services;

import com.greenloop.user_service.dtos.UserStatsDTO;
import com.greenloop.user_service.exceptions.UserNotFoundException;
import com.greenloop.user_service.models.User;
import com.greenloop.user_service.repos.UserRepository;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserStatsService {

    private final UserRepository userRepo;

    public UserStatsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Get user statistics
    public UserStatsDTO getUserStats(UUID userId) {

        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        UserStatsDTO userStatsDTO = new UserStatsDTO();
        userStatsDTO.setTotalCoins(user.getTotalCoins());
        userStatsDTO.setEventsAttended(user.getEventsAttended());
        userStatsDTO.setEventsRegistered(user.getEventsRegistered());

        return userStatsDTO;
    }

}