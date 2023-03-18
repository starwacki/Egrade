package com.github.starwacki.global.open_api;

import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.http.MediaType;

class TeacherControllerComponents {

    private TeacherControllerComponents() {}

    static ApiResponse getTeacherClassesResponse() {
        return new ApiResponse()
                .description("Classes are successfully received")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default",new Example()
                                        .value("[{\"year\":2023,\"className\":\"1A\"}," +
                                                "{\"year\":2022,\"className\":\"2A\"}]"))));
    }

    static ApiResponse teacherNotFoundResponse() {
        return new ApiResponse()
                .description("Not found")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example().value(
                                        "{ \"message\": \"Not found teacher with id: 1\"}"))));
    }

    static ApiResponse addSchoolClassToTeacherResponse() {
        return new ApiResponse()
                .description("Class was successfully added to teacher ");
    }

    static ApiResponse  getAllTeachersInformationResponse() {
        return new ApiResponse()
                .description("Teachers information was successfully received")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example().value(
                                        "[{\"firstname\":\"Jan\",\"lastname\":\"Kowalski\",\"subject\":\"PHYSICS\",\"phone\":\"111222333\"," +
                                                "\"email\":\"JanKowalski@wp.pl\"},{\"firstname\":\"Sebastian\",\"lastname\":\"Nowak\",\"subject\":" +
                                                "\"PHYSICS\",\"phone\":\"333222111\",\"email\":\"SebastianNowak@wp.pl\"}]"))));
    }


    static RequestBody addSchoolClassToTeacherRequestBody() {
        return new RequestBody()
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema<>())
                                .addExamples("default",new Example()
                                        .value("[{\"firstname\": Jan,\"lastname\":\"\"}," +
                                                "{\"firstname\": Łukasz Kowal,\"className\":\"2A\"}]"))));
    }


}
