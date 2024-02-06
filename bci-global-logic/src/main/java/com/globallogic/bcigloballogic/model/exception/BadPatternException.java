package com.globallogic.bcigloballogic.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect pattern")
public class BadPatternException extends RuntimeException {

    private String message;

    public BadPatternException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
