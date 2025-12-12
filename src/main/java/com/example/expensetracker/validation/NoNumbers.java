package com.example.expensetracker.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy= NoNumbersValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoNumbers {   // defines our custom valid Annotation, and shows the message when validation fails
    String message() default "InvalidTitle, title must not contain numbers";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
