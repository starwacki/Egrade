package com.github.starwacki.components.student.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record StudentGradesDTO(
        String firstname,
        String lastname,
        int year,
        String className,
        List<SubjectDTO> subjectGrades
) {

}
