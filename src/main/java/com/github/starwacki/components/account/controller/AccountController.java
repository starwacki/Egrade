package com.github.starwacki.components.account.controller;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.global.model.account.Role;
import com.github.starwacki.components.account.service.AccountService;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Controller
@RequestMapping("/account")
public class AccountController implements AccountOperations {

    private final AccountService accountService;

    @Secured(value = {"ADMIN"})
    @PostMapping("/student")
    public ResponseEntity<AccountViewDTO> addStudent(AccountStudentDTO studentDTO) {
        AccountViewDTO student = accountService.saveStudentAndParentAccount(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);

    }

    @Secured(value = {"ADMIN"})
    @PostMapping("/students")
    public ResponseEntity<List<AccountViewDTO>> addStudentsFromCSVFile(String pathname) {
      List<AccountViewDTO> list = accountService.saveStudentsAndParentsFromFile(pathname);
      return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @Secured(value = {"ADMIN"})
    @PostMapping("/teacher")
    public ResponseEntity<AccountViewDTO> addTeacher(AccountTeacherDTO accountTeacherDTO) {
        AccountViewDTO teacher = accountService.saveTeacherAccount(accountTeacherDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(teacher);
    }

    @Secured(value = {"ADMIN"})
    @GetMapping("/{role}={id}")
    public ResponseEntity<AccountViewDTO> getAccountById(
            @PathVariable Role role,
            @PathVariable int id) {
        AccountViewDTO accountViewDTO = accountService.getAccountById(role,id);
        return ResponseEntity.ok(accountViewDTO);
    }

    @Secured(value = {"ADMIN"})
    @DeleteMapping("/{role}={id}")
    public ResponseEntity<AccountViewDTO> deleteAccountById(
            @PathVariable Role role,
            @PathVariable int id) {
        AccountViewDTO accountViewDTO = accountService.deleteAccountById(role,id);
        return ResponseEntity.ok(accountViewDTO);
    }


    //Todo - implement jwt token
    @PermitAll
    @PutMapping("password/{role}={id}")
    public ResponseEntity<AccountViewDTO> changeAccountPassword(
            @PathVariable Role role,
            @PathVariable int id,
            @RequestParam String oldPassword,
            @RequestParam @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{6,25}$") String newPassword) {
        AccountViewDTO accountViewDTO = accountService.changeAccountPassword(role,id,oldPassword,newPassword);
        return ResponseEntity.ok(accountViewDTO);
    }



}
