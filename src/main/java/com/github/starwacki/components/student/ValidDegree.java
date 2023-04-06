package com.github.starwacki.components.student;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DegreeValidator.class)
public @interface ValidDegree {
    String message() default "Invalid grade value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}