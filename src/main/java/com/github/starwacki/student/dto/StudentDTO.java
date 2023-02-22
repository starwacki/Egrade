package com.github.starwacki.student.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public record StudentDTO(
        int id,
        String firstname,
        String lastname,
        String className,
        int year,
        String parentPhone
) {
}
