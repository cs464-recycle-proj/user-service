package com.greenloop.user_service.dtos;

import lombok.Data;

@Data
public class UserStatsDTO {
    // use for passing user stats to ui
    private int totalCoins;
    private int eventsAttended;
    private int eventsRegistered;

}
