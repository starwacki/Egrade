package com.github.starwacki.components.teacher.dto;

import lombok.Builder;

@Builder
public record TeacherResponseDTO(
        String firstname,
        String lastname,
        String subject,
        String phone,
        String email
) {
}
