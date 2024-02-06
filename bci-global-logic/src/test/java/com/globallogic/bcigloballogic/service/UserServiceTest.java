package com.globallogic.bcigloballogic.service;

import com.globallogic.bcigloballogic.model.entity.UserEntity;
import com.globallogic.bcigloballogic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("test load user by username - user found")
    public void testLoadUserByUsernameUserFound() {
        String userEmail = "test@example.com";
        String userPassword = "testPassword";
        UserEntity mockUserEntity = UserEntity.builder()
                .email(userEmail)
                .password(userPassword)
                .build();

        when(userRepository.findUserEntityByEmail(userEmail)).thenReturn(Optional.of(mockUserEntity));
        UserDetails userDetails = userService.loadUserByUsername(userEmail);

        assertEquals(userEmail, userDetails.getUsername());
        assertEquals(userPassword, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(userRepository, times(1)).findUserEntityByEmail(userEmail);
    }

    @Test
    @DisplayName("test load user by username - user not found")
    public void testLoadUserByUsername_UserNotFound() {
        String userEmail = "test@example.com";

        when(userRepository.findUserEntityByEmail(userEmail)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(userEmail));

        assertEquals("User not found with email: " + userEmail, exception.getMessage());

        verify(userRepository, times(1)).findUserEntityByEmail(userEmail);
    }
}
