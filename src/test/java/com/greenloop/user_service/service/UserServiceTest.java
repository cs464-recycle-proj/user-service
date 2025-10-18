package com.greenloop.user_service.service;

import com.greenloop.user_service.enums.UserRole;
import com.greenloop.user_service.models.User;

import com.greenloop.user_service.repos.UserEventHistoryRepository;
import com.greenloop.user_service.repos.UserInterestRepository;
import com.greenloop.user_service.repos.UserRepository;
import com.greenloop.user_service.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepo;
    private UserInterestRepository interestRepo;
    private UserEventHistoryRepository historyRepo;
    private UserService service;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class);
        interestRepo = mock(UserInterestRepository.class);
        historyRepo = mock(UserEventHistoryRepository.class);
        service = new UserService(userRepo, interestRepo, historyRepo);
    }

    @Test
    void testCreateUser() {
        User user = new User("alice", "alice@gmail.com", UserRole.USER);
        UUID id = UUID.randomUUID();
        user.setId(id);

        when(userRepo.save(any(User.class))).thenReturn(user);

        User created = service.createUser(user);
        assertNotNull(created);
        assertEquals("alice", created.getUsername());
        assertEquals(id, created.getId());
    }

    @Test
    void testGetUserById() {
        UUID id = UUID.randomUUID();
        User user = new User("bob", "bob@gmail.com", UserRole.USER);
        user.setId(id);
        user.setUsername("bob");

        when(userRepo.findById(id)).thenReturn(Optional.of(user));

        User found = service.getUserById(id);
        assertNotNull(found);
        assertEquals("bob", found.getUsername());
    }

    @Test
    void testRecommendEvents() {
        UUID userId = UUID.randomUUID();
        List<UUID> allEvents = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        when(interestRepo.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<UUID> recommended = service.recommendEvents(userId, allEvents);
        assertEquals(5, recommended.size());
    }
}
