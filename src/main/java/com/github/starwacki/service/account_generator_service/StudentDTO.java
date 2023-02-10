package com.github.starwacki.service.account_generator_service;


import lombok.Builder;

@Builder
public record StudentDTO(String firstname, String lastname,int year, String className, String parentPhoneNumber) {
}
