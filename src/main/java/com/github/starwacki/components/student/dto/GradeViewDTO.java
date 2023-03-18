package com.github.starwacki.components.student.dto;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public record GradeViewDTO(
        String description,
        int weight,
        String degree,
        String addedDate,
        String addedBy
) {
}
