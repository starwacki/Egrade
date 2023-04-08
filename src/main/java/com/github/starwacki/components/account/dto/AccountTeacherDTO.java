package com.github.starwacki.components.account.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;


@Builder
public record AccountTeacherDTO(
        @Pattern(regexp = "^[A-Za-z]{3,40}$") String firstname,
        @Pattern(regexp = "^[A-Za-z]{3,40}$") String lastname,
        @NotBlank @Size(min = 9, max = 9) @Pattern(regexp = "^\\d+$") String workPhone,
        @NotBlank @Email String email,
        @NotNull String subject
        ) {
}
