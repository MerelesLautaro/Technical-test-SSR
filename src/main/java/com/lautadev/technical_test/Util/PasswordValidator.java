package com.lautadev.technical_test.Util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        List<String> errors = new ArrayList<>();

        boolean containsWhitespace = !password.matches("^\\S*$");
        if (containsWhitespace) {
            errors.add("Password cannot contain whitespace");
        }

        if (password.length() >= 128) {
            errors.add("Password must be less than 128 characters long");
        }

        if (password.length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }

        if (!errors.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (String error : errors) {
                context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
            }
            return false;
        }

        return true;
    }
}