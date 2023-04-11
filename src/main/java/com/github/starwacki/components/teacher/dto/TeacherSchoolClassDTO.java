package com.github.starwacki.components.teacher.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Builder
public record TeacherSchoolClassDTO(
        @Range(min = 2020, max = 2040) int year,
        @Pattern(regexp = "^[1-9][A-Z]$") @Length(max = 2) String className
) {
}
