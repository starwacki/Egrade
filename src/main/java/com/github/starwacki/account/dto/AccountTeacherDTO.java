package com.github.starwacki.account.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountTeacherDTO(
     @NotBlank String firstname,
     @NotBlank String lastname,
     @NotBlank String workPhone,
     @NotBlank String email
) {
}
