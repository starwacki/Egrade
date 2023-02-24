package com.github.starwacki.components.student.dto;

import lombok.Builder;
@Builder
public record SchoolClassDTO(
        String name,
        int classYear) {
}
