package com.github.starwacki.components.account.dto;


import lombok.Builder;

@Builder
public record AccountResponseDTO(
        int id,
        String firstname,
        String lastname,
        String username,
        String password,
        String accountType
) {
}
