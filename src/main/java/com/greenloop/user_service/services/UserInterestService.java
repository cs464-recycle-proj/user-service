package com.greenloop.user_service.services;

import com.greenloop.user_service.exceptions.UserNotFoundException;
import com.greenloop.user_service.models.User;
import com.greenloop.user_service.models.UserInterest;
import com.greenloop.user_service.repos.UserInterestRepository;
import com.greenloop.user_service.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserInterestService {

    private final UserInterestRepository interestRepo;
    private final UserRepository userRepo;

    public UserInterestService(UserInterestRepository interestRepo, UserRepository userRepo) {
        this.interestRepo = interestRepo;
        this.userRepo = userRepo;
    }

    // get user interests
    public List<UserInterest> getInterests(UUID userId) {
        return interestRepo.findByUserId(userId);
    }

    // add a list of user interests
    public List<UserInterest> addInterests(UUID userId, List<String> interests) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        List<UserInterest> userInterests = new ArrayList<>();
        for (String interest : interests) {

            UserInterest ui = interestRepo.findByUserIdAndInterest(userId, interest).orElse(new UserInterest(user, interest));
            userInterests.add(ui);
        }
        return interestRepo.saveAll(userInterests);
    }

}