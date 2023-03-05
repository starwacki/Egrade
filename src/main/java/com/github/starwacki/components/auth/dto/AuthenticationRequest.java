package com.github.starwacki.components.auth.dto;

import lombok.Builder;

@Builder
public record AuthenticationRequest(
        String username,
        String password
) {
}
