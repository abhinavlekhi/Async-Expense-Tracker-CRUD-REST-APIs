package com.example.expensetracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoNumbersValidator implements ConstraintValidator<NoNumbers, String> {

    @Override
    public boolean isValid( String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotBlank will handle null / empty values
        }
        return !value.matches(".*\\d.*"); // returns false if any digit is found
    }

}
