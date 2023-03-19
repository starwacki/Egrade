package com.github.starwacki.components.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationRequest(
       @NotBlank String username,
       @NotBlank String password
) {
}
