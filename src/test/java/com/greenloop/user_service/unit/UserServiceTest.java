package com.greenloop.user_service.unit;

import com.greenloop.user_service.dtos.*;
import com.greenloop.user_service.enums.Interest;
import com.greenloop.user_service.exceptions.UserNotFoundException;
import com.greenloop.user_service.models.User;
import com.greenloop.user_service.models.UserInterest;
import com.greenloop.user_service.repos.UserInterestRepository;
import com.greenloop.user_service.repos.UserRepository;
import com.greenloop.user_service.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInterestRepository interestRepository;

    @InjectMocks
    private UserService userService;

    private UUID testUserId;
    private User testUser;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;
    private UpdateAvatarRequest updateAvatarRequest;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();

        testUser = User.builder()
                .id(testUserId)
                .email("test@example.com")
                .username("testuser")
                .birthDate(LocalDate.of(1990, 1, 1))
                .avatarUrl("https://example.com/avatar.jpg")
                .bio("Test bio")
                .eventsAttended(5)
                .eventsRegistered(10)
                .interests(new ArrayList<>())
                .build();

        createUserRequest = CreateUserRequest.builder()
                .username("testuser")
                .birthDate(LocalDate.of(1990, 1, 1))
                .avatarUrl("https://example.com/avatar.jpg")
                .bio("Test bio")
                .build();

        updateUserRequest = UpdateUserRequest.builder()
                .username("updateduser")
                .bio("Updated bio")
                .birthDate(LocalDate.of(1991, 2, 2))
                .build();

        updateAvatarRequest = UpdateAvatarRequest.builder()
                .avatarUrl("https://example.com/new-avatar.jpg")
                .build();
    }

    // ===== GET ALL USERS TESTS =====
    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Given
        List<User> users = Arrays.asList(testUser, createAnotherUser());
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserResponse> result = userService.getAllUsers();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserResponse> result = userService.getAllUsers();

        // Then
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findAll();
    }

    // ===== GET USER BY ID TESTS =====
    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // When
        UserResponse result = userService.getUserById(testUserId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(testUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with ID " + testUserId + " was not found.");

        verify(userRepository, times(1)).findById(testUserId);
    }

    // ===== CREATE USER TESTS =====
    @Test
    void createUser_ShouldCreateAndReturnUser() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse result = userService.createUser(
                createUserRequest,
                testUserId,
                "test@example.com");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getUsername()).isEqualTo("testuser");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldUseProvidedCredentials() {
        // Given
        UUID authUserId = UUID.randomUUID();
        String authEmail = "auth@example.com";
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        UserResponse result = userService.createUser(
                createUserRequest,
                authUserId,
                authEmail);

        // Then
        assertThat(result.getId()).isEqualTo(authUserId);
        assertThat(result.getEmail()).isEqualTo(authEmail);
        verify(userRepository, times(1)).save(argThat(user -> user.getId().equals(authUserId) &&
                user.getEmail().equals(authEmail)));
    }

    // ===== UPDATE USER TESTS =====
    @Test
    void updateUser_ShouldUpdateAllFields_WhenAllProvided() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse result = userService.updateUser(testUserId, updateUserRequest);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).save(testUser);
        assertThat(testUser.getUsername()).isEqualTo("updateduser");
        assertThat(testUser.getBio()).isEqualTo("Updated bio");
    }

    @Test
    void updateUser_ShouldUpdateOnlyProvidedFields() {
        // Given
        UpdateUserRequest partialUpdate = UpdateUserRequest.builder()
                .username("newusername")
                .build();

        String originalBio = testUser.getBio();
        LocalDate originalBirthDate = testUser.getBirthDate();

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse result = userService.updateUser(testUserId, partialUpdate);

        // Then
        assertThat(testUser.getUsername()).isEqualTo("newusername");
        assertThat(testUser.getBio()).isEqualTo(originalBio);
        assertThat(testUser.getBirthDate()).isEqualTo(originalBirthDate);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(testUserId, updateUserRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with ID " + testUserId + " was not found.");

        verify(userRepository, never()).save(any(User.class));
    }

    // ===== UPDATE AVATAR TESTS =====
    @Test
    void updateAvatar_ShouldUpdateAvatarUrl() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse result = userService.updateAvatar(testUserId, updateAvatarRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(testUser.getAvatarUrl()).isEqualTo("https://example.com/new-avatar.jpg");
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateAvatar_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateAvatar(testUserId, updateAvatarRequest))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with ID " + testUserId + " was not found.");
    }

    // ===== DELETE USER TESTS =====
    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Given
        when(userRepository.existsById(testUserId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(testUserId);

        // When
        userService.deleteUser(testUserId);

        // Then
        verify(userRepository, times(1)).existsById(testUserId);
        verify(userRepository, times(1)).deleteById(testUserId);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.existsById(testUserId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(testUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with ID " + testUserId + " was not found.");

        verify(userRepository, never()).deleteById(any());
    }

    // ===== GET INTERESTS TESTS =====
    @Test
    void getInterests_ShouldReturnUserInterests() {
        // Given
        List<UserInterest> interests = createUserInterests();
        when(interestRepository.findByUserId(testUserId)).thenReturn(interests);

        // When
        UserInterestsResponse result = userService.getInterests(testUserId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getInterests()).hasSize(2);
        assertThat(result.getInterests()).contains("RECYCLING", "TREE_PLANTING");
        verify(interestRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    void getInterests_ShouldReturnEmptyList_WhenNoInterests() {
        // Given
        when(interestRepository.findByUserId(testUserId)).thenReturn(Collections.emptyList());

        // When
        UserInterestsResponse result = userService.getInterests(testUserId);

        // Then
        assertThat(result.getInterests()).isEmpty();
        verify(interestRepository, times(1)).findByUserId(testUserId);
    }

    // ===== ADD INTERESTS TESTS =====
    @Test
    void addInterests_ShouldAddNewInterests() {
        // Given
        List<String> newInterests = Arrays.asList("RECYCLING", "WORKSHOP");
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // Mock for RECYCLING
        when(interestRepository.existsByUserIdAndInterest(
                eq(testUserId), 
                eq(Interest.RECYCLING))) 
                .thenReturn(false);

        // Mock for WORKSHOP
        when(interestRepository.existsByUserIdAndInterest(
                eq(testUserId), 
                eq(Interest.WORKSHOP))) 
                .thenReturn(false);

        when(interestRepository.saveAll(anyList())).thenAnswer(i -> i.getArguments()[0]);

        // When
        UserInterestsResponse result = userService.addInterests(testUserId, newInterests);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getInterests()).hasSize(2);
        
        // Verify findById is called once
        verify(userRepository, times(1)).findById(testUserId);
        
        // Verify existsBy... is called once for each interest with the correct enum
        verify(interestRepository, times(1)).existsByUserIdAndInterest(testUserId, Interest.RECYCLING);
        verify(interestRepository, times(1)).existsByUserIdAndInterest(testUserId, Interest.WORKSHOP);
        
        // Verify saveAll is called once
        verify(interestRepository, times(1)).saveAll(anyList());
    }

    @Test
    void addInterests_ShouldThrowException_WhenUserNotFound() {
        // Given
        List<String> interests = Arrays.asList("RECYCLING");
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.addInterests(testUserId, interests))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with ID " + testUserId + " was not found.");

        verify(interestRepository, never()).saveAll(anyList());
    }

    // ===== HELPER METHODS =====
    private User createAnotherUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .email("another@example.com")
                .username("anotheruser")
                .birthDate(LocalDate.of(1992, 3, 3))
                .build();
    }

    private List<UserInterest> createUserInterests() {
        UserInterest interest1 = UserInterest.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .interest(Interest.RECYCLING)
                .build();

        UserInterest interest2 = UserInterest.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .interest(Interest.TREE_PLANTING)
                .build();

        return Arrays.asList(interest1, interest2);
    }
}