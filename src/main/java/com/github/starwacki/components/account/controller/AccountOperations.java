package com.github.starwacki.components.account.controller;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.global.model.account.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/account")
interface AccountOperations {

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Add student, parent account",
            description = "Operation add manually student  and dedicated parent account from information" +
                          "given in AccountStudentDTO operation. Is only available for ADMIN role",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(ref = "accountStudentDTO"),
            responses = {
                    @ApiResponse(responseCode = "201",ref = "createStudentResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @PostMapping("/student")
    ResponseEntity<AccountViewDTO> addStudent(@RequestBody @Valid AccountStudentDTO studentDTO);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Add student, parent account from csv file.",
            description = "Operation add all students  and dedicated parents account from csv file." +
                          " Is only available for ADMIN role. Csv file should be table with rows: " +
                          " |firstname|lastname|year|className|parentPhoneNumber|",
            parameters =
                     @Parameter(name = "pathname",description = "pathname of csv file with data"),
            responses = {
                    @ApiResponse(responseCode = "201",ref = "createStudentsResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestAddStudentsFromCSVFileResponse")
            }
    )
    @PostMapping("/students")
    ResponseEntity<List<AccountViewDTO>> addStudentsFromCSVFile(@RequestParam @NotBlank String pathname);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Add teacher account",
            description = "Operation add manually teacher account from information" +
                          " given in AccountTeacherDTO. Is only available for ADMIN role",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(ref = "accountTeacherDTO"),
            responses = {
                    @ApiResponse(responseCode = "201",ref = "createTeacherResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestResponse")
            }
    )
    @PostMapping("/teacher")
    ResponseEntity<AccountViewDTO> addTeacher(@RequestBody @Valid AccountTeacherDTO accountTeacherDTO);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Get account information",
            description = "Operation return account information with given role and id." +
                          " Is only available for ADMIN role",
            parameters = {
                    @Parameter(name = "role",description = "Enum role in string. Get ADMIN account is illegal - return exception",
                               schema = @Schema(allowableValues = {"STUDENT", "TEACHER", "PARENT"})),
                    @Parameter(name = "id",description = "Account id")
                    },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "getAccountByIdResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestGetAccountResponse"),
                    @ApiResponse (responseCode = "404",ref = "accountNotFoundResponse")
            }
    )
    @GetMapping("/{role}={id}")
    ResponseEntity<AccountViewDTO> getAccountById(@PathVariable Role role, @PathVariable int id);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Delete account",
            description = "Operation delete account by given id. If you delete student account, dedicated" +
                    " parent account is removed too. " + "Is only available for ADMIN role",
            parameters = {
                    @Parameter(name = "role",description = "Enum role in string. Delete ADMIN account is illegal - return exception",
                            schema = @Schema(allowableValues = {"STUDENT", "TEACHER", "PARENT"})),
                    @Parameter(name = "id",description = "Account id")
            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "deleteAccountByIdResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestDeleteAccountResponse"),
                    @ApiResponse (responseCode = "404",ref = "accountNotFoundResponse")
            }
    )
    @DeleteMapping("/{role}={id}")
    ResponseEntity<AccountViewDTO> deleteAccountById(@PathVariable Role role, @PathVariable int id);

    @Operation(
            security = @SecurityRequirement(name = "BearerJWT"),
            summary = "Change account password",
            description = "Operation change account password by given role and id. " +
                    "Is  available for any role",
            parameters = {
                    @Parameter(name = "role",description = "Enum role in string.Change ADMIN password is illegal.",
                            schema = @Schema(allowableValues = {"STUDENT", "TEACHER", "PARENT"})),
                    @Parameter(name = "id",description = "Account to changed id"),
                    @Parameter(name = "oldPassword",description = "Actual account password"),
                    @Parameter(name = "newPassword",description = "New password")

            },
            responses = {
                    @ApiResponse(responseCode = "200",ref = "deleteAccountByIdResponse"),
                    @ApiResponse(responseCode = "403", ref = "forbiddenResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestChangeAccountPasswordResponse"),
                    @ApiResponse (responseCode = "404",ref = "accountNotFoundResponse")
            }
    )
    @PutMapping("/{role}={id}")
    ResponseEntity<AccountViewDTO> changeAccountPassword(
            @PathVariable Role role,
            @PathVariable int id,
            @RequestParam String oldPassword,
            @RequestParam @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{6,25}$") String newPassword);




}

