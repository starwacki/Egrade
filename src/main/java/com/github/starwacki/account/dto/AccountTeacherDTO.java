package com.github.starwacki.account.dto;

import com.github.starwacki.model.grades.Subject;
import jakarta.validation.constraints.*;
import lombok.Builder;


@Builder
public record AccountTeacherDTO(
        @Pattern(regexp = "^[A-Za-z]{3,40}$") String firstname,
        @Pattern(regexp = "^[A-Za-z]{3,40}$") String lastname,
        @NotBlank @Size(max = 10) String workPhone,
        @Email String email,
        @NotNull Subject subject
        ) {
}
