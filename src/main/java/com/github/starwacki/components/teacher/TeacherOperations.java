package com.github.starwacki.components.teacher.controller;

import com.github.starwacki.components.teacher.dto.SchoolClassDTO;
import com.github.starwacki.components.teacher.dto.TeacherDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/teacher")
interface TeacherOperations {

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get all teacher-taught classes",
            description = "Operation receive all the classes the teacher teaches." +
                    " Is available for any role",
            parameters = {
                    @Parameter(name = "id", description = "Teacher id"),
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "getTeacherClassesResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "teacherNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @GetMapping("/id={id}/classes")
    ResponseEntity<List<SchoolClassDTO>> getTeacherClasses(@PathVariable int id);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Add class to Teacher",
            description = "Operation add new class to the teacher. The Class should be created before adding." +
                    " Is only available for ADMIN role",
            parameters = {
                    @Parameter(name = "id", description = "Teacher id"),
            },
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(ref = "addSchoolClassToTeacherRequestBody"),
            responses = {
                    @ApiResponse(responseCode = "204",ref = "addSchoolClassToTeacherResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "teacherNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @PutMapping("/id={id}/classes")
    ResponseEntity<?> addSchoolClassToTeacher(
            @PathVariable int id,
            @RequestBody @Valid SchoolClassDTO schoolClassDTO);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get teachers information",
            description = "Operation receive information about all teachers." +
                    " Is available for any role",
            responses = {
                    @ApiResponse(responseCode = "200",ref = "getAllTeachersInformationResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
            }
    )
    @GetMapping("/teachers")
   ResponseEntity<List<TeacherDTO>> getAllTeachersInformation();
}
