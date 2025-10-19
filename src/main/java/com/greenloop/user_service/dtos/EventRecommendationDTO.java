package com.greenloop.user_service.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class EventRecommendationDTO {
    private UUID id;
    private double score;

    public EventRecommendationDTO(UUID id, double score) {
        this.id = id;
        this.score = score;
    }
    
}
