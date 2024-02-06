package com.globallogic.bcigloballogic.util;

import com.globallogic.bcigloballogic.model.exception.BadPatternException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

public class UserDataValidator {

    private static final Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern passwordPattern = Pattern.compile("^(?=(?:[^A-Z]*[A-Z]){1})(?=(?:[^\\d]*\\d){2})[a-zA-Z\\d]{8,12}$");

    public static void emailPattern(String email) throws ResponseStatusException {
        if (email == null || !emailPattern.matcher(email).matches()) {
            throw new BadPatternException("The Email format is invalid");
        }
    }

    public static void passwordPattern(String password) throws ResponseStatusException {
        if (password == null || !passwordPattern.matcher(password).matches()) {
            throw new BadPatternException( "The Password format is invalid");
        }
    }
}
