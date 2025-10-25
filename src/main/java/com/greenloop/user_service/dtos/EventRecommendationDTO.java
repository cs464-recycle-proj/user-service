package com.greenloop.user_service.dtos;

import lombok.Data;

import java.util.*;

@Data
public class EventRecommendationDTO {
    private UUID userId;
    private double score;

    public EventRecommendationDTO(UUID userId, double score) {
        this.userId = userId;
        this.score = score;
    }
    
}
