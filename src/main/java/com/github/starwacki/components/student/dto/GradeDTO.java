package com.github.starwacki.components.student.dto;

import com.github.starwacki.components.student.model.Subject;
import com.github.starwacki.components.student.validator.ValidGrade;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record GradeDTO(
        @NotNull @Size(min = 1,max = 50) String description,
        @Range(min = 1, max = 100) int weight,
        @NotNull Subject subject,
        @ValidGrade double degree,
        int addingTeacherId
) {
}
