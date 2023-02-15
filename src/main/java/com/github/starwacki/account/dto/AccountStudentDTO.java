package com.github.starwacki.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record AccountStudentDTO(
      @Pattern(regexp = "^[A-Za-z]{3,40}$") String firstname,
      @Pattern(regexp = "^[A-Za-z]{3,40}$")  String lastname,
      @Range(min = 2020, max = 2040) int year,
      @Pattern(regexp = "^[1-9][A-Z]$")  String className,
      @NotBlank @Size(max = 10) String parentPhoneNumber) {
}
