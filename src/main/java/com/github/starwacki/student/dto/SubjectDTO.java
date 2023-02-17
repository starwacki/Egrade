package com.github.starwacki.student.dto;

import com.github.starwacki.student.model.Subject;
import lombok.Builder;

import java.util.List;

@Builder
public record SubjectDTO(
        Subject subject,
        List<GradeViewDTO> grades,
        String gradeAverage
) {
}
