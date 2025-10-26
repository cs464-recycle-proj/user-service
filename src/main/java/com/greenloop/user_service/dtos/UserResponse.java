package com.greenloop.user_service.dtos;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String avatarUrl;
    private String bio;
    private LocalDate birthDate;
    private LocalDate joinedDate;
    private int eventsAttended;
    private int eventsRegistered;
}
