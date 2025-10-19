package com.greenloop.user_service.dtos;

import com.greenloop.user_service.enums.UserAction;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
public class UserActionDTO {
    private UUID userId;
    private UUID eventId;
    private UserAction action;
    private Integer rating;
}
