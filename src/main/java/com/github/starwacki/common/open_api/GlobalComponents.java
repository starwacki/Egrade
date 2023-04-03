package com.github.starwacki.common.open_api;

import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.http.MediaType;

class GlobalComponents {

    private GlobalComponents() {
    }

    static ApiResponse badRequestResponse() {
        return new ApiResponse()
                .description("Bad request - validation or wrong input data")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("validation", new Example().value(
                                        "{ \"lastname\": \"Invalid value: musi pasować do wyrażenia ^[A-Za-z]{3,40}$\"}"
                                ))));
    }

    static ApiResponse accountNotFoundResponse() {
        return new ApiResponse()
                .description("Not found")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example().value(
                                        "{ \"message\": \"Account not found id: 1\"}"
                                ))));
    }

    static ApiResponse forbiddenResponse() {
        return new ApiResponse()
                .description("Forbidden - user not have permissions to do this operation");
    }
}
