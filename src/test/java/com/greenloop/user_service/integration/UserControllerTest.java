package com.greenloop.user_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenloop.user_service.dtos.*;
import com.greenloop.user_service.services.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final UUID testUserId = UUID.randomUUID();

    // ===== ENDPOINT MAPPING TESTS =====
    @Test
    void createUser_ShouldReturnCreated_WhenValidRequest() throws Exception {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
                .username("newuser")
                .birthDate(LocalDate.of(1995, 5, 5))
                .build();

        UserResponse response = UserResponse.builder()
                .id(testUserId)
                .email("test@example.com")
                .username("newuser")
                .build();

        when(userService.createUser(any(), eq(testUserId), eq("test@example.com")))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/users")
                .header("X-User-ID", testUserId.toString())
                .header("X-User-Email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.id").value(testUserId.toString()))
                .andExpect(jsonPath("$.data.username").value("newuser"));

        verify(userService, times(1)).createUser(any(), eq(testUserId), eq("test@example.com"));
    }

    @Test
    void getAllUsers_ShouldReturnOk() throws Exception {
        // Given
        List<UserResponse> users = Arrays.asList(
                UserResponse.builder().id(UUID.randomUUID()).username("user1").build(),
                UserResponse.builder().id(UUID.randomUUID()).username("user2").build());

        when(userService.getAllUsers()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserProfile_ShouldReturnOk() throws Exception {
        // Given
        UserResponse response = UserResponse.builder()
                .id(testUserId)
                .username("testuser")
                .build();

        when(userService.getUserById(testUserId)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/users/profile")
                .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));

        verify(userService, times(1)).getUserById(testUserId);
    }

    @Test
    void updateUserProfile_ShouldReturnOk() throws Exception {
        // Given
        UpdateUserRequest request = UpdateUserRequest.builder()
                .username("updated")
                .build();

        UserResponse response = UserResponse.builder()
                .id(testUserId)
                .username("updated")
                .build();

        when(userService.updateUser(eq(testUserId), any())).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/users/profile")
                .header("X-User-ID", testUserId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("updated"));

        verify(userService, times(1)).updateUser(eq(testUserId), any());
    }

    @Test
    void updateUserAvatar_ShouldReturnOk() throws Exception {
        // Given
        UpdateAvatarRequest request = UpdateAvatarRequest.builder()
                .avatarUrl("https://example.com/avatar.jpg")
                .build();

        UserResponse response = UserResponse.builder()
                .id(testUserId)
                .avatarUrl("https://example.com/avatar.jpg")
                .build();

        when(userService.updateAvatar(eq(testUserId), any())).thenReturn(response);

        // When & Then
        mockMvc.perform(patch("/api/users/avatar")
                .header("X-User-ID", testUserId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Avatar updated successfully"));

        verify(userService, times(1)).updateAvatar(eq(testUserId), any());
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(testUserId);

        // When & Then
        mockMvc.perform(delete("/api/users")
                .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(testUserId);
    }

    @Test
    void getInterests_ShouldReturnOk() throws Exception {
        // Given
        UserInterestsResponse response = UserInterestsResponse.builder()
                .interests(Arrays.asList("RECYCLING", "TREE_PLANTING"))
                .build();

        when(userService.getInterests(testUserId)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/users/interests")
                .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.interests", hasSize(2)));

        verify(userService, times(1)).getInterests(testUserId);
    }

    @Test
    void addInterests_ShouldReturnCreated() throws Exception {
        // Given
        List<String> interests = Arrays.asList("RECYCLING", "WORKSHOP");
        UserInterestsResponse response = UserInterestsResponse.builder()
                .interests(interests)
                .build();

        when(userService.addInterests(eq(testUserId), anyList())).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/users/interests")
                .header("X-User-ID", testUserId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.interests", hasSize(2)));

        verify(userService, times(1)).addInterests(eq(testUserId), anyList());
    }

    @Test
    void checkHealth_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("User Service is Up and Running!"));
    }
}