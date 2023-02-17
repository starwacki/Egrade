package com.github.starwacki.student.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GradeViewDTO(
        String description,
        double degree,
        int weight,
        LocalDate addedDate,
        String addedBy
) {
}
