package com.lautadev.technical_test.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage(), "Not authorized");
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<Object> handleInvalidFilterException(InvalidFilterException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid filter", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), "User Not Found or Deleted");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        String message = "Invalid password";
        StringBuilder details = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.append(error.getDefaultMessage()).append("; ");
        }

        if (details.isEmpty()) {
            details.append("No specific error.");
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, details.toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}

