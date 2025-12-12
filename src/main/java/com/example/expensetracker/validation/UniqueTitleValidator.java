package com.example.expensetracker.validation;

import com.example.expensetracker.repository.ExpenseRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String> {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null || title.trim().isEmpty()) {
            return true; // @NotBlank will handle null / empty values
        }
        return !expenseRepository.existsByExpenseTitle(title);
    }

}
