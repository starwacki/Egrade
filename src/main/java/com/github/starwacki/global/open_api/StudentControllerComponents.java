package com.github.starwacki.global.open_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.student.dto.GradeViewDTO;
import com.github.starwacki.components.student.dto.StudentGradesDTO;
import com.github.starwacki.components.student.dto.SubjectDTO;
import com.github.starwacki.global.model.grades.Subject;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class StudentControllerComponents {

    private StudentControllerComponents() {}

    static ApiResponse getAllStudentsFromClassResponse() {
        return new ApiResponse()
                .description("Students are successfully received")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default",new Example()
                                        .value("[{\"id\":1,\"firstname\":\"Firstname1\",\"lastname\":\"Lastname1\",\"className\":\"1A\",\"year\":\"2023\",\"parentPhone\":\"123456789\"}," +
                                                "{\"id\":2,\"firstname\":\"Firstname2\",\"lastname\":\"Lastname2\",\"className\":\"1A\",\"year\":\"2023\",\"parentPhone\":\"123456789\"}]"))));
    }

    static ApiResponse changeStudentClassResponse() {
        return new ApiResponse()
                .description("Class was successfully changed");
    }

    static ApiResponse studentNotFoundResponse() {
        return new ApiResponse()
                .description("Not found")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example().value(
                                        "{ \"message\": \"Student not found id: 1\"}"))));
    }

    static ApiResponse addGradeToStudentResponse() {
        return new ApiResponse()
                .description("Grade was successfully added")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example()
                                        .value("{\"description\":\"Grade from the term exam\",\" weight\":\"5\",\"subject\":\"MATHS\",\"degree\":\"5+\",\"addingTeacherId\":\"2\"}"))));
    }

    static ApiResponse getStudentGradeResponse() {
        return new ApiResponse()
                .description("Grade was successfully received")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example()
                                        .value("{\"description\":\"Grade from the term exam\",\"weight\":\"5\",\"degree\":\"5+\",\"addedDate\":\"2022-03-18\",\"addedBy\":\"Jan Kowalski\"}"))));
    }

    static ApiResponse updateStudentGradeResponse() {
        return new ApiResponse()
                .description("Grade was successfully updated")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example()
                                        .value("{\"description\":\"Grade from the term exam\",\"weight\":\"5\",\"degree\":\"5+\",\"addedDate\":\"2022-03-18\",\"addedBy\":\"Jan Kowalski\"}"))));
    }

    static ApiResponse deleteStudentGradeResponse() {
        return new ApiResponse()
                .description("Grade was successfully deleted")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example()
                                        .value("{\"description\":\"Grade from the term exam\",\"weight\":\"5\",\"degree\":\"5+\",\"addedDate\":\"2022-03-18\",\"addedBy\":\"Jan Kowalski\"}"))));
    }


    static ApiResponse getStudentGradesResponse() {
        return new ApiResponse()
                .description("Grades was successfully received")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example()
                                                .value("{\"firstname\":\"Jan\",\"lastname\":\"Kowalski\",\"year\":2023,\"className\":\"2A\",\"subjectGrades\":[{\"subject\":\"MATHS\"," +
                                                        "\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}," +
                                                        "{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"gradeAverage\"" +
                                                        ":\"3,0\"},{\"subject\":\"ENGLISH\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"" +
                                                        "addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}]," +
                                                        "\"gradeAverage\":\"3,0\"},{\"subject\":\"GERMAN\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\"," +
                                                        "\"addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}]," +
                                                        "\"gradeAverage\":\"3,0\"},{\"subject\":\"HISTORY\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\"," +
                                                        "\"addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}]," +
                                                        "\"gradeAverage\":\"3,0\"},{\"subject\":\"GEOGRAPHY\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\"," +
                                                        "\"addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"" +
                                                        "gradeAverage\":\"3,0\"},{\"subject\":\"BIOLOGY\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"" +
                                                        "addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"" +
                                                        "gradeAverage\":\"3,0\"},{\"subject\":\"CHEMISTRY\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"" +
                                                        "addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"" +
                                                        "gradeAverage\":\"3,0\"},{\"subject\":\"PHYSICS\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"" +
                                                        "addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"" +
                                                        "gradeAverage\":\"3,0\"},{\"subject\":\"ART\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"" +
                                                        "addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"" +
                                                        "gradeAverage\":\"3,0\"},{\"subject\":\"PHYSICAL_EDUCATION\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\"," +
                                                        "\"addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"" +
                                                        "gradeAverage\":\"3,0\"},{\"subject\":\"ECONOMY\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"" +
                                                        "addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"" +
                                                        "gradeAverage\":\"3,0\"},{\"subject\":\"RELIGION\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"" +
                                                        "addedBy\":\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"" +
                                                        "gradeAverage\":\"3,0\"},{\"subject\":\"COMPUTER_SCIENCE\",\"grades\":[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"addedBy\":" +
                                                        "\"Janusz Kowal\"},{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}],\"" +
                                                        "gradeAverage\":\"3,0\"}]}")
                                        )));
    }

    static ApiResponse getStudentSubjectGrades() {
        return new ApiResponse()
                .description("Subject grades was successfully received")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default", new Example()
                                        .value("{\"firstname\":\"Jan\",\"lastname\":\"Kowalski\",\"year\":2023,\"className\":\"2A\",\"subjectGrades\":[{\"subject\":\"MATHS\",\"grades\":" +
                                                "[{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"1\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}," +
                                                "{\"description\":\"Grade for exam\",\"weight\":3,\"degree\":\"5\",\"addedDate\":\"2022-10-10\",\"addedBy\":\"Janusz Kowal\"}]," +
                                                "\"gradeAverage\":\"3.0\"}]}"))));
    }



    static RequestBody changeStudentClassRequestBody() {
        return new RequestBody()
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema<>())
                                .addExamples("default",new Example()
                                        .value("{\"description\":\"Grade from the term exam\",\" weight\":\"5\",\"subject\":\"MATHS\",\"degree\":\"5+\",\"addingTeacherId\":\"2\"}"))));
    }

    static RequestBody updateStudentGradeRequestBody() {
        return new RequestBody()
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema<>())
                                .addExamples("default",new Example()
                                        .value("{\"description\":\"Grade from the term exam\",\" weight\":\"5\",\"subject\":\"MATHS\",\"degree\":\"5+\",\"addingTeacherId\":\"2\"}"))));
    }

}
