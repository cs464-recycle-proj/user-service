package com.greenloop.user_service.controllers;

import com.greenloop.user_service.models.UserInterest;
import com.greenloop.user_service.services.UserInterestService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users/interests")
public class UserInterestController {

    private final UserInterestService interestService;

    public UserInterestController(UserInterestService interestService) {
        this.interestService = interestService;
    }

    // get all interest of user
    @GetMapping
    public List<UserInterest> getInterests(@RequestHeader("X-User-ID") String userId) {
        return interestService.getInterests(UUID.fromString(userId));
    }

    // add user's new interest
    @PostMapping
    public List<UserInterest> addInterests(@RequestHeader("X-User-ID") String userId, @RequestBody List<String> interests) {
        return interestService.addInterests(UUID.fromString(userId), interests);
    }



}