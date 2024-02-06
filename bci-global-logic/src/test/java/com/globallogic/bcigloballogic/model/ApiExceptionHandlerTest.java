package com.globallogic.bcigloballogic.model;

import com.globallogic.bcigloballogic.model.exception.ApiExceptionHandler;
import com.globallogic.bcigloballogic.model.exception.BadPatternException;
import com.globallogic.bcigloballogic.model.exception.DuplicatedUserException;
import com.globallogic.bcigloballogic.model.response.ApiErrorResponse;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiExceptionHandlerTest {
    private final ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();

    @Test
    public void testAuthorizationException() {
        JwtException jwtException = new JwtException("Unauthorized");
        ResponseEntity<ApiErrorResponse> response = apiExceptionHandler.authorizationException(jwtException);

        assertErrorResponse(response, HttpStatus.UNAUTHORIZED, jwtException.getMessage());
    }

    @Test
    public void testBadPatternException() {
        BadPatternException badPatternException = new BadPatternException("Bad Request");
        ResponseEntity<ApiErrorResponse> response = apiExceptionHandler.BadPatternException(badPatternException);

        assertErrorResponse(response, HttpStatus.BAD_REQUEST, badPatternException.getMessage());
    }

    @Test
    public void testDuplicatedUserException() {
        DuplicatedUserException duplicatedUserException = new DuplicatedUserException("Conflict");
        ResponseEntity<ApiErrorResponse> response = apiExceptionHandler.duplicatedUserException(duplicatedUserException);

        assertErrorResponse(response, HttpStatus.CONFLICT, duplicatedUserException.getMessage());
    }

    @Test
    public void testUnhandledException() {
        RuntimeException unhandledException = new RuntimeException("Internal Server Error");
        ResponseEntity<ApiErrorResponse> response = apiExceptionHandler.unhandledException(unhandledException);

        assertErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, unhandledException.getMessage());
    }

    private void assertErrorResponse(ResponseEntity<ApiErrorResponse> response, HttpStatus expectedHttpStatus, String expectedMessage) {
        assertEquals(expectedHttpStatus, response.getStatusCode());
        ApiErrorResponse apiErrorResponse = response.getBody();
        assertEquals(expectedHttpStatus.value(), apiErrorResponse.getCode());
        assertEquals(expectedMessage, apiErrorResponse.getDetail());
        assertEquals(Timestamp.class, apiErrorResponse.getTimestamp().getClass());
    }
}
