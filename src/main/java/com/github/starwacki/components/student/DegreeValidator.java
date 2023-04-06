package com.github.starwacki.components.student;

import com.github.starwacki.common.model.grades.Degree;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DegreeValidator implements ConstraintValidator<ValidDegree,String> {

    private static final List<String> ALLOWED_GRADES = Arrays.stream(Degree.values()).map(degree -> degree.getSymbol())
            .collect(Collectors
                    .toList());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ALLOWED_GRADES.contains(value);
    }

}
