package com.github.starwacki.components.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Builder
public record AccountStudentRequestDTO(
      @Pattern(regexp = "^[A-Za-z]{3,40}$") String firstname,
      @Pattern(regexp = "^[A-Za-z]{3,40}$")  String lastname,
      @Range(min = 2020, max = 2040) int year,
      @Pattern(regexp = "^[1-9][A-Z]$") @Length(max = 2) String className,
      @NotBlank @Size(min = 9, max = 9) @Pattern(regexp = "^\\d+$") String parentPhoneNumber) {
}
