package com.github.starwacki.account.dto;


import lombok.Builder;

@Builder
public record AccountViewDTO(
        int id,
        String firstname,
        String lastname,
        String username,
        String password,
        String accountType
) {
}
