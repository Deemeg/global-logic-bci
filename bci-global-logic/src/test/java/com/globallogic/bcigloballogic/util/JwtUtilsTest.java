package com.globallogic.bcigloballogic.util;

import com.globallogic.bcigloballogic.configuration.EnvironmentConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtUtilsTest {
    private JwtUtils jwtUtils;

    private EnvironmentConfig environmentConfig;

    @BeforeEach
    public void setUp() {
        environmentConfig = Mockito.mock(EnvironmentConfig.class);
        when(environmentConfig.getExpiration()).thenReturn(100000000L);
        when(environmentConfig.getSecret()).thenReturn("tokenSecret123132131231sadsadsadsasadasdsaasdsadsasad");
        jwtUtils = new JwtUtils(environmentConfig);
    }

    @Test
    @DisplayName("generateToken")
    public void testGenerateToken() {
        String email = "test@example.com";
        String token = jwtUtils.generateToken(email);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    @DisplayName("isValidToken")
    public void testIsValidToken() {
        String token = jwtUtils.generateToken("test@example.com");
        boolean isValid = jwtUtils.isTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("invalidToken")
    public void testInvalidToken() {
        String invalidToken = "invalidToken";

        assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> jwtUtils.isTokenValid(invalidToken));
    }

    @Test
    @DisplayName("get username from token")
    public void testGetUsernameFromToken() {
        String email = "test@example.com";
        String token = jwtUtils.generateToken(email);

        String username = jwtUtils.getUsernameFromToken(token);
        assertEquals(email, username);
    }

    @Test
    @DisplayName("get username from token with invalid token")
    public void testGetUsernameFromTokenInvalidToken() {
        String invalidToken = "invalidToken";

        assertThrows(MalformedJwtException.class,
                () -> jwtUtils.getUsernameFromToken(invalidToken));
    }

    @Test
    @DisplayName("get username from token with expired token")
    public void testGetUsernameFromTokenExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired token
                .signWith(SignatureAlgorithm.HS256, environmentConfig.getSecret())
                .compact();

        assertThrows(ExpiredJwtException.class,
                () -> jwtUtils.getUsernameFromToken(expiredToken));
    }
}
