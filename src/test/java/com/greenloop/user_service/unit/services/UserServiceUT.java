package com.greenloop.user_service.unit.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.*;

import java.util.*;
import java.time.LocalDate;

import com.greenloop.user_service.repos.UserRepository;
import com.greenloop.user_service.services.UserService;
import com.greenloop.user_service.dtos.UserDTO;
import com.greenloop.user_service.exceptions.UserNotFoundException;
import com.greenloop.user_service.models.User;


class UserServiceUT {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setName("Sonia");
        user.setEmail("sonia@example.com");
    }

    @Test
    void testGetAllUsers() {
        when(userRepo.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals("Sonia", result.get(0).getName());
    }

    @Test
    void testGetUserById_Found() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);
        assertEquals("sonia@example.com", result.getEmail());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepo.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void testCreateUser() {
        UserDTO dto = new UserDTO();
        dto.setName("Sonia");
        dto.setBio("Hello");
        dto.setAvatarUrl("avatar.png");
        dto.setBday(LocalDate.of(2000, 1, 1));

        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User created = userService.createUser(dto, userId, "sonia@example.com");

        assertEquals("Sonia", created.getName());
        assertEquals("sonia@example.com", created.getEmail());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Found() {
        User updated = new User();
        updated.setName("Updated Name");
        updated.setEmail("new@example.com");

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(userId, updated);

        assertEquals("Updated Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepo.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, user));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepo).deleteById(userId);
        userService.deleteUser(userId);
        verify(userRepo, times(1)).deleteById(userId);
    }
}