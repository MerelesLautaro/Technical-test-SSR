package com.lautadev.technical_test.Exception;

public class InvalidFilterException extends RuntimeException {
    public InvalidFilterException(String message) {
        super(message);
    }
}

