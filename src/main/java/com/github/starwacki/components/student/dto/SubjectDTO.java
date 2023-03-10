package com.github.starwacki.components.student.dto;

import com.github.starwacki.global.model.grades.Subject;
import lombok.Builder;
import java.util.List;

@Builder
public record SubjectDTO(
        Subject subject,
        List<GradeViewDTO> grades,
        String gradeAverage
) {
}
