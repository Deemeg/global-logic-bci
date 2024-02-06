package com.globallogic.bcigloballogic.util;

import com.globallogic.bcigloballogic.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserService userService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("test doFilter - valid token")
    public void testDoFilterInternalValidToken() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);

        String token = "validToken";
        String username = "testUser";
        UserDetails userDetails = createMockUserDetails(username);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.isTokenValid(token)).thenReturn(true);
        when(jwtUtils.getUsernameFromToken(token)).thenReturn(username);
        when(userService.loadUserByUsername(username)).thenReturn(userDetails);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).isTokenValid(token);
        verify(jwtUtils, times(1)).getUsernameFromToken(token);
        verify(userService, times(1)).loadUserByUsername(username);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("test doFilter - invalid token")
    public void testDoFilterInternalInvalidToken() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);

        String token = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.isTokenValid(token)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).isTokenValid(token);
        verify(jwtUtils, never()).getUsernameFromToken(token);
        verify(userService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("test doFilter - empty token")
    public void testDoFilterInternalNoToken() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);

        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, never()).isTokenValid(anyString());
        verify(jwtUtils, never()).getUsernameFromToken(anyString());
        verify(userService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    private UserDetails createMockUserDetails(String username) {
        return new org.springframework.security.core.userdetails.User(
                username,
                "password",
                true,
                true,
                true,
                true,
                Collections.emptyList()
        );
    }
}
