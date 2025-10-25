package com.greenloop.user_service.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserDTO {
    // used for user creation
    private String name;
    private String avatarUrl;
    private String bio;
    private LocalDate bday;
    
}
