package com.github.starwacki.student.dto;

import com.github.starwacki.student.model.Subject;
import lombok.Builder;
import java.util.List;
import java.util.Map;

@Builder
public record StudentGradesDTO(
        String firstname,
        String lastname,
        int year,
        String className,
        Map<Subject, List<GradeDTO>> grades
) {

}
