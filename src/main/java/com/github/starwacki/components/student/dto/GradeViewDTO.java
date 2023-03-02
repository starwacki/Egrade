package com.github.starwacki.components.student.dto;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public record GradeViewDTO(
        String description,
        String degree,
        int weight,
        LocalDate addedDate,
        String addedBy
) {
}
