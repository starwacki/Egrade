package com.github.starwacki.components.account.dto;

import com.github.starwacki.components.student.model.Subject;
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
