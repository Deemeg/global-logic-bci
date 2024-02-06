package com.globallogic.bcigloballogic.model.exception;

import com.globallogic.bcigloballogic.model.response.ApiErrorResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({JwtException.class, AuthenticationException.class})
    public ResponseEntity<ApiErrorResponse> authorizationException(Exception e) {
        ApiErrorResponse errorInfo = new ApiErrorResponse(new Timestamp(System.currentTimeMillis()), HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({BadPatternException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> BadPatternException(Exception e) {
        ApiErrorResponse errorInfo = new ApiErrorResponse(new Timestamp(System.currentTimeMillis()), HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DuplicatedUserException.class})
    public ResponseEntity<ApiErrorResponse> duplicatedUserException(Exception e) {
        ApiErrorResponse errorInfo = new ApiErrorResponse(new Timestamp(System.currentTimeMillis()), HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiErrorResponse> unhandledException(Exception e) {
        ApiErrorResponse errorInfo = new ApiErrorResponse(new Timestamp(System.currentTimeMillis()), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
