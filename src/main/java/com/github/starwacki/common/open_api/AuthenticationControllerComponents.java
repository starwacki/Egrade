package com.github.starwacki.common.open_api;

import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.http.MediaType;

class AuthenticationControllerComponents {

    private AuthenticationControllerComponents() {}

    static ApiResponse okAuthenticateUserResponse() {
        return new ApiResponse()
                .description("User was successfully authenticated and JWT cookie has been added")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example().value(
                                        "{\"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiU3R1ZGVudFRlc3RTVFUxIiwiaWF0IjoxNjc5MTQ4NjAwLCJleHAiOjE2NzkyMzUwMDB9.mxQlCANbW-cGuFfLtz59BDD0AewrUKWiNcp1jPTKFNU\"}"
                                ))));
    }

    static ApiResponse badRequestAuthenticateUserResponse() {
        return new ApiResponse()
                .description("Bad request")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example().value(
                                        "{\n" +
                                                "  \"type\": \"about:blank\",\n" +
                                                "  \"title\": \"Bad Request\",\n" +
                                                "  \"status\": 400,\n" +
                                                "  \"detail\": \"Failed to read request\",\n" +
                                                "  \"instance\": \"/auth/authenticate\"\n" +
                                                "}"
                                ))));
    }

    static ApiResponse unauthorizedAuthenticateUserResponse() {
        return new ApiResponse()
                .description("User has not been authenticated")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example().value(
                                        "Wrong username or password"
                                        ))));
    }

    static RequestBody authenticateUserRequestBody() {
        return new RequestBody()
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema<>())
                                .addExamples("default",new Example()
                                        .value("{\"username\":\"StudentTestSTU1\",\"password\":\"123456\"}"))
                ));
    }
}
