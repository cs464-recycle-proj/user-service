package com.greenloop.user_service.controllers;

import com.greenloop.user_service.dtos.UserStatsDTO;
import com.greenloop.user_service.services.UserStatsService;

import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/users/stats")
public class UserStatsController {

    private final UserStatsService statsService;

    public UserStatsController(UserStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public UserStatsDTO getUserStats(@RequestHeader("X-User-ID") String userId) {
        return statsService.getUserStats(UUID.fromString(userId));
    }

}