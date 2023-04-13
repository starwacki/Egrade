package com.github.starwacki.components.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token,
        @JsonIgnore String accountRole
) {
}
