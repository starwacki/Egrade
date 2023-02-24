package com.github.starwacki.components.student.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class GradeValidator implements ConstraintValidator<ValidGrade,Double> {

    private static final List<Double> ALLOWED_GRADES = Arrays.asList(1.0, 1.5, 1.75, 2.0, 2.5, 2.75, 3.0, 3.5, 3.75, 4.0, 4.5, 4.75, 5.0,5.5,5.75,6.0);

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return ALLOWED_GRADES.contains(value);
    }

}
