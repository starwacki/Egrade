package com.github.starwacki.components.student.dto;

import com.github.starwacki.components.student.model.Subject;
import com.github.starwacki.components.student.validator.ValidGrade;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record GradeDTO(
        String description,
        @Range(min = 1, max = 100) int weight,
        @NotNull Subject subject,
        @ValidGrade double degree,
        int addingTeacherId
) {
}