package com.github.starwacki.student.dto;

import lombok.Builder;

@Builder
public record SchoolClassDTO(
        String name,
        int classYear) {
}
