package com.github.starwacki.service.account_generator_service.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

import java.time.Year;

@Builder
public record AccountStudentDTO(
      @NotBlank String firstname,
      @NotBlank  String lastname,
      @Range(min = 2020, max = 2040) int year,
      @NotBlank  String className,
        String parentPhoneNumber) {
}
