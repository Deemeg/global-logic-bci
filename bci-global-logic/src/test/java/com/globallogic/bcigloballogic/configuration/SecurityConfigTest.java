package com.globallogic.bcigloballogic.configuration;

import com.globallogic.bcigloballogic.controller.AuthController;
import com.globallogic.bcigloballogic.repository.UserRepository;
import com.globallogic.bcigloballogic.util.JwtAuthEntryPoint;
import com.globallogic.bcigloballogic.util.JwtAuthFilter;
import com.globallogic.bcigloballogic.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {
    private SecurityConfig securityConfig;
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @BeforeEach
    public void setUp() {
        jwtAuthEntryPoint = Mockito.mock(JwtAuthEntryPoint.class);
        securityConfig = new SecurityConfig(jwtAuthEntryPoint);
    }

    @Test
    public void testPasswordEncoderBean() {
        MockitoAnnotations.openMocks(this);
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testJwtAuthFilterBean() {
        MockitoAnnotations.openMocks(this);
        JwtAuthFilter jwtAuthFilter = securityConfig.jwtAuthFilter();
        assertNotNull(jwtAuthFilter);
    }
}
