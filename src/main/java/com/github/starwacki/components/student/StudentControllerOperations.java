package com.github.starwacki.components.student;

import com.github.starwacki.components.student.dto.StudentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/student")
public interface StudentControllerOperations {

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get information about students",
            description = "Operation receive all information of students in given class. " +
                    "Is only available for ADMIN or TEACHER role",
            parameters = {
                    @Parameter(name = "className",description = "Class name, numbered as follows: NUMBER(0-9) + BIG LETTER (A-Z)"),
                    @Parameter(name = "classYear",description = "the current year of the student necessary to be added to the appropriate class "),
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "getAllStudentsFromClassResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @GetMapping("/class={className}&year={classYear}")
    ResponseEntity<List<StudentDTO>> getAllStudentsFromClass(
            @PathVariable @Pattern(regexp = "^[1-9][A-Z]$")  String className,
            @PathVariable @Min(2020) @Max(2040)  int classYear);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Change student class",
            description = "Operation changed student class." +
                          " Is only available for ADMIN Or TEACHER Role ",
            parameters = {
                    @Parameter(name = "className",description = "Class name, numbered as follows: NUMBER(0-9) + BIG LETTER (A-Z)"),
                    @Parameter(name = "classYear",description = "The current year of the student necessary to be added to the appropriate class "),
                    @Parameter(name = "id",description = "Account to changed id "),
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "changeStudentClassResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @PutMapping("/id={id}/class")
    ResponseEntity<?> changeStudentClass(
            @PathVariable int id,
            @RequestParam @Pattern(regexp = "^[1-9][A-Z]$") String className,
            @RequestParam @Min(2020) @Max(2040) int classYear);



}
