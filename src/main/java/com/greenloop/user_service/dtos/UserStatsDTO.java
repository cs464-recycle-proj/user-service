package com.greenloop.user_service.dtos;

import lombok.Data;

@Data
public class UserStatsDTO {
    // TODO need userid?
    private int totalCoins;
    private int eventsAttended;
    private int eventsRegistered;

}
