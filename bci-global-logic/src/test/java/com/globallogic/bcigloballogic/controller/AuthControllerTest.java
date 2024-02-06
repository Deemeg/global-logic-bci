package com.globallogic.bcigloballogic.controller;

import com.globallogic.bcigloballogic.model.dto.LoginDto;
import com.globallogic.bcigloballogic.model.dto.PhoneDto;
import com.globallogic.bcigloballogic.model.dto.RegisterDto;
import com.globallogic.bcigloballogic.model.entity.PhoneEntity;
import com.globallogic.bcigloballogic.model.entity.UserEntity;
import com.globallogic.bcigloballogic.model.exception.BadPatternException;
import com.globallogic.bcigloballogic.model.exception.DuplicatedUserException;
import com.globallogic.bcigloballogic.model.response.LoginResponse;
import com.globallogic.bcigloballogic.model.response.SignUpResponse;
import com.globallogic.bcigloballogic.repository.PhoneRepository;
import com.globallogic.bcigloballogic.repository.UserRepository;
import com.globallogic.bcigloballogic.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class AuthControllerTest {

    private AuthController loginController;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JwtUtils jwtUtil;

    private AuthenticationManager authenticationManager;

    private PhoneRepository phoneRepository;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtUtil = Mockito.mock(JwtUtils.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        phoneRepository = Mockito.mock(PhoneRepository.class);
        loginController = new AuthController( userRepository, phoneRepository, passwordEncoder,  jwtUtil,  authenticationManager);
    }

    @Test
    @DisplayName("Test Register - new user")
    public void testRegister() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setName("Test");
        registerDto.setPassword("Test12aaa");
        registerDto.setPhones(Arrays.asList(new PhoneDto(123456789L, 123, "54")));

        when(userRepository.findUserEntityByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Test12aaa")).thenReturn("encodedPassword");
        when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(createMockUserEntity());
        when(jwtUtil.generateToken("test@example.com")).thenReturn("mockToken");
        when(phoneRepository.save(ArgumentMatchers.any(PhoneEntity.class))).thenReturn(new PhoneEntity());

        ResponseEntity<SignUpResponse> responseEntity = loginController.register(registerDto);
        SignUpResponse signUpResponse = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(signUpResponse.getToken(), "mockToken");
        assertNotNull(signUpResponse.getId());
        assertNotNull(signUpResponse.getCreated());
        assertNotNull(signUpResponse.getLastLogin());
        assertFalse(signUpResponse.isActive());

        verify(userRepository, times(1)).findUserEntityByEmail("test@example.com");
        verify(passwordEncoder, times(1)).encode("Test12aaa");
        verify(userRepository, times(1)).save(ArgumentMatchers.any(UserEntity.class));
        verify(jwtUtil, times(1)).generateToken("test@example.com");
    }

    @Test
    @DisplayName("Test Register - invalidEmailFormat")
    public void testRegisterInvalidEmailFormat() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("example.com");


        Throwable exception = assertThrows(
                BadPatternException.class,
                () -> {
                    loginController.register(registerDto);
                }
        );
        assertEquals("The Email format is invalid", exception.getMessage());
    }

    @Test
    @DisplayName("Test Register - invalidPasswordFormat")
    public void testRegisterInvalidPasswordFormat() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("Test12aaa");
        when(userRepository.findUserEntityByEmail("test@example.com")).thenReturn(Optional.ofNullable(createMockUserEntity()));

        Throwable exception = assertThrows(
                DuplicatedUserException.class,
                () -> {
                    loginController.register(registerDto);
                }
        );
        assertEquals("the email is already in use", exception.getMessage());
    }

    @Test
    @DisplayName("Test Register - email already used")
    public void testRegisterEmailAlreadyUsed() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("as456asfaAAAA");

        Throwable exception = assertThrows(
                BadPatternException.class,
                () -> {
                    loginController.register(registerDto);
                }
        );
        assertEquals("The Password format is invalid", exception.getMessage());
    }

    @Test
    @DisplayName("Test login - login OK")
    public void testLogin() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("Test12aaa");

        UserEntity mockUserEntity = createMockUserEntity();
        Authentication mockAuthentication = createMockAuthentication();

        when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);
        when(userRepository.findUserEntityByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(mockUserEntity));
        when(jwtUtil.generateToken(ArgumentMatchers.anyString())).thenReturn("mockToken");
        when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(mockUserEntity);

        ResponseEntity<LoginResponse> responseEntity = loginController.login(loginDto);
        LoginResponse loginResponse = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(loginResponse.getToken(), "mockToken");
        assertNotNull(loginResponse.getId());
        assertNotNull(loginResponse.getCreated());
        assertNotNull(loginResponse.getLastLogin());
        assertTrue(loginResponse.isActive());

        verify(authenticationManager, times(1)).authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findUserEntityByEmail("test@example.com");
        verify(jwtUtil, times(1)).generateToken("test@example.com");
        verify(userRepository, times(1)).save(ArgumentMatchers.any(UserEntity.class));
    }

    private UserEntity createMockUserEntity() {
        return  UserEntity.builder()
                .id(UUID.randomUUID())
                .isActive(false)
                .email("test@example.com")
                .created(Timestamp.from(Instant.now()))
                .lastLogin(Timestamp.from(Instant.now()))
                .name("")
                .phones(Collections.emptyList())
                .build();
    }

    private Authentication createMockAuthentication() {
        return Mockito.mock(Authentication.class);
    }
}
