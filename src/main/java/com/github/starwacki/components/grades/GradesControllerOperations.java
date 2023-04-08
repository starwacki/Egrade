package com.github.starwacki.components.grades;

import com.github.starwacki.components.grades.dto.GradeDTO;
import com.github.starwacki.components.grades.dto.GradeViewDTO;
import com.github.starwacki.components.grades.dto.SubjectDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/grades")
interface GradesControllerOperations {

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Add grade to student",
            description = "Operation add new grade to student. " +
                    "Is only available for ADMIN or TEACHER role",
            parameters =
            @Parameter(name = "studentID",description = "Student id")
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
    @PostMapping("/grade")
    ResponseEntity<GradeDTO> addGradeToStudent(
            @RequestBody @Valid GradeDTO gradeDTO);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get one student grade",
            description = "Operation receive one student grade." +
                    " Is  available for any role",
            parameters = {
                    @Parameter(name = "studentID", description = "Student id"),
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
    @GetMapping("/student={studentID}/{gradeID}")
    ResponseEntity<GradeViewDTO> getStudentGrade(
            @PathVariable int studentID,
            @PathVariable int gradeID);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Update grade",
            description = "Operation updated student grade." +
                    " Is only available for ADMIN or TEACHER role",
            parameters = {
                    @Parameter(name = "studentID", description = "Student id"),
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
    @PutMapping("/student={studentID}/{gradeID}")
    ResponseEntity<GradeDTO> updateStudentGrade(
            @PathVariable int studentID,
            @PathVariable int gradeID,
            @RequestBody @Valid GradeDTO gradeDTO);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Delete grade",
            description = "Operation deleted student grade." +
                    " Is only available for ADMIN or TEACHER role",
            parameters = {
                    @Parameter(name = "studentID", description = "Student id"),
                    @Parameter(name = "gradeID", description = "Id of grade to delete")
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "deleteStudentGradeResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @DeleteMapping("/student={studentID}/{gradeID}")
    ResponseEntity<GradeDTO> deleteStudentGrade(
            @PathVariable int studentID,
            @PathVariable int gradeID);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get all students grades",
            description = "Operation receive student grades of all subjects." +
                    " Is available for any role",
            parameters = {
                    @Parameter(name = "studentID", description = "Student id"),
                    @Parameter(name = "gradeID",description = "Grade to delete id")
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "getStudentGradesResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @GetMapping("/student={studentID}")
    ResponseEntity<List<SubjectDTO>> getStudentGrades(@PathVariable int studentID);


    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get students grades of one subject",
            description = "Operation receive student grades of one subjects." +
                    " Is available for any role",
            parameters = {
                    @Parameter(name = "studentID", description = "Student id"),
                    @Parameter(name = "subjectID", description = "Id of subject to receive ( Enum ordinal )"),
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "getStudentSubjectGrades"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse(responseCode = "404", ref = "studentNotFoundResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @GetMapping("/student={studentID}/subject={subjectID}")
    ResponseEntity<SubjectDTO> getStudentSubjectGrades(
            @PathVariable int studentID,
            @PathVariable int subjectID);

}
