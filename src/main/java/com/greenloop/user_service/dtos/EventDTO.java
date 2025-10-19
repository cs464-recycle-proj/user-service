package com.greenloop.user_service.dtos;

import lombok.*;
import java.util.*;

@Getter
@Setter
public class EventDTO {
    
    private UUID id;
    private String eventName;
    private List<String> tags;
    private int attendeeCount;  

}
