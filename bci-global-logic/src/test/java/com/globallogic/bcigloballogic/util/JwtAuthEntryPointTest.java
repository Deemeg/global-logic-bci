package com.globallogic.bcigloballogic.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import static org.mockito.Mockito.*;

public class JwtAuthEntryPointTest {

    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @BeforeEach
    public void setUp() {
        jwtAuthEntryPoint = new JwtAuthEntryPoint();
    }

    @Test
    public void testCommence() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);

        jwtAuthEntryPoint.commence(request, response, authException);

        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
