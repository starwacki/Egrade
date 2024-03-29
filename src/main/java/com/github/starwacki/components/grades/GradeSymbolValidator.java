package com.github.starwacki.components.grades;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class GradeSymbolValidator implements ConstraintValidator<GradeSymbolValid,String> {

    private static final List<String> ALLOWED_GRADES = Arrays.stream(GradeSymbolValue.values()).map(gradeSymbolValue -> gradeSymbolValue.getSymbol())
            .collect(Collectors
                    .toList());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ALLOWED_GRADES.contains(value);
    }

}
