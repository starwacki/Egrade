package com.github.starwacki.components.grades.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record SubjectResponseDTO(
        String subject,
        List<GradeResponeDTO> grades,
        String gradeAverage
) {
}
