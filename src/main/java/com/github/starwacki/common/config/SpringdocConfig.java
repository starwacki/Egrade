package com.github.starwacki.common.config;


import com.github.starwacki.common.open_api.OpenApiService;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SpringdocConfig {

    private final OpenApiService openApiService;

    @Bean
    public OpenAPI baseOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Egrade documentation")
                .version("1.0.0")
                .description("Documentation for Egrade Spring boot project"))
                .tags(getDocumentationTags())
                .components(components());
    }



    private List<Tag> getDocumentationTags() {
        return List.of(new Tag()
                       .name("account-controller")
                       .description("Account controller is used for all account operations: add new account, delete account, change password etc.."),
                new Tag()
                        .name("student-controller")
                        .description("Student controller provides operations related to student: get students grades, add grades, change student information etc.."));
    }

    private Components components() {
        return new Components()
                .responses(openApiService.getAllApiResponses())
                .requestBodies(openApiService.getAllApiRequestBodies());
    }



}
