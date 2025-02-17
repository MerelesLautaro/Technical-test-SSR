package com.lautadev.technical_test.Exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends RuntimeException {

    private final HttpStatus status;

    public UnauthorizedAccessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public UnauthorizedAccessException(String message, Throwable cause, HttpStatus status){
        super(message, cause);
        this.status = status;
    }

    public static UnauthorizedAccessException accessDenied(){
        return new UnauthorizedAccessException("Access Denied",HttpStatus.UNAUTHORIZED);
    }
}
