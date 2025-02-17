package com.lautadev.technical_test.Exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException userNotFound() {
        return new UserNotFoundException("User Not Found");
    }
}

