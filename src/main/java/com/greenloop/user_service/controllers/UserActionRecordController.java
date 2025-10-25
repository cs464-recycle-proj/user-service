package com.greenloop.user_service.controllers;

import com.greenloop.user_service.models.UserActionRecord;
import com.greenloop.user_service.services.UserActionRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users/actions")
public class UserActionRecordController {

    private final UserActionRecordService actionService;

    public UserActionRecordController(UserActionRecordService actionService) {
        this.actionService = actionService;
    }

    @GetMapping
    public List<UserActionRecord> getUserActionRecords(@RequestHeader("X-User-ID") String userId) {
        return actionService.getUserActionRecords(UUID.fromString(userId));
    }

    @PostMapping
    public UserActionRecord addUserActionRecord(@RequestHeader("X-User-ID") String userId, @RequestBody UserActionRecord actionRecord) {
        return actionService.addUserActionRecord(UUID.fromString(userId), actionRecord);
    }

}