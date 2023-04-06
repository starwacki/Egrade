package com.github.starwacki.components.student;

import com.github.starwacki.components.student.dto.GradeDTO;
import com.github.starwacki.components.student.dto.GradeViewDTO;
import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.components.student.dto.StudentGradesDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/student")
public interface StudentOperations {

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


    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Add grade to student",
            description = "Operation add new grade to student. " +
                          "Is only available for ADMIN or TEACHER role",
            parameters =
                    @Parameter(name = "id",description = "Student id")
            ,
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(ref = "changeStudentClassRequestBody"),
            responses = {
                    @ApiResponse(responseCode = "201",ref = "addGradeToStudentResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @PostMapping("/id={id}/grade")
    ResponseEntity<GradeDTO> addGradeToStudent(
            @PathVariable int id,
            @RequestBody @Valid GradeDTO gradeDTO);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get one student grade",
            description = "Operation receive one student grade." +
                    " Is  available for any role",
            parameters = {
                    @Parameter(name = "id", description = "Student id"),
                    @Parameter(name = "gradeID", description = "Id of grade to receive")
            }
            ,
            responses = {
                    @ApiResponse(responseCode = "200",ref = "getStudentGradeResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @GetMapping("/id={id}/grade={gradeID}")
    ResponseEntity<GradeViewDTO> getStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Update grade",
            description = "Operation updated student grade." +
                          " Is only available for ADMIN or TEACHER role",
            parameters = {
                    @Parameter(name = "id", description = "Student id"),
                    @Parameter(name = "gradeID", description = "Id of grade to update")
            },
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Only degree,weight and description can be update" ,ref = "updateStudentGradeRequestBody")
            ,
            responses = {
                    @ApiResponse(responseCode = "200",ref = "updateStudentGradeResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @PutMapping("/id={id}/grade={gradeID}")
    ResponseEntity<GradeDTO> updateStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID,
            @RequestBody @Valid GradeDTO gradeDTO);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Delete grade",
            description = "Operation deleted student grade." +
                    " Is only available for ADMIN or TEACHER role",
            parameters = {
                    @Parameter(name = "id", description = "Student id"),
                    @Parameter(name = "gradeID", description = "Id of grade to delete")
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "deleteStudentGradeResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @DeleteMapping("/id={id}/grade={gradeID}")
    ResponseEntity<GradeDTO> deleteStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get all students grades",
            description = "Operation receive student grades of all subjects." +
                    " Is available for any role",
            parameters = {
                    @Parameter(name = "id", description = "Student id"),
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "getStudentGradesResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @GetMapping("/id={id}/grades")
    ResponseEntity<StudentGradesDTO> getStudentGrades(@PathVariable int id);


    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get students grades of one subject",
            description = "Operation receive student grades of one subjects." +
                    " Is available for any role",
            parameters = {
                    @Parameter(name = "id", description = "Student id"),
                    @Parameter(name = "subjectID", description = "Id of subject to receive ( Enum ordinal )"),
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "getStudentSubjectGrades"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @GetMapping("/id={id}/grades/subject={subjectID}")
    ResponseEntity<StudentGradesDTO> getStudentSubjectGrades(
            @PathVariable int id,
            @PathVariable int subjectID);

}
