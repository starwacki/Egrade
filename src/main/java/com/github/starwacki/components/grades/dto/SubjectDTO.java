package com.github.starwacki.components.grades.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record SubjectDTO(
        String subject,
        List<GradeViewDTO> grades,
        String gradeAverage
) {
}
