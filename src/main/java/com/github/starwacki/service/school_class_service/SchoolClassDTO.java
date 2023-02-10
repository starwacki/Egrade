package com.github.starwacki.service.school_class_service;

import lombok.Builder;

@Builder
public record SchoolClassDTO(String name, int classYear) {
}
