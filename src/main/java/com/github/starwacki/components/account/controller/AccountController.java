package com.github.starwacki.components.account.controller;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.global.model.account.Role;
import com.github.starwacki.components.account.service.AccountService;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class AccountController {

    private final AccountService accountService;

    @RolesAllowed(value = {"ADMIN"})
    @PostMapping("/student")
    ResponseEntity<AccountViewDTO> addStudent(
            @RequestBody @Valid AccountStudentDTO studentDTO) {
        AccountViewDTO student = accountService.saveStudentAndParentAccount(studentDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(student);
    }

    @RolesAllowed(value = {"ADMIN"})
    @PostMapping("/students")
    ResponseEntity<List<AccountViewDTO>> addStudentsFromCSVFile(
            @RequestParam @NotBlank String pathname) {
      List<AccountViewDTO> list = accountService.saveStudentsAndParentsFromFile(pathname);
      return ResponseEntity
              .status(HttpStatus.CREATED)
              .body(list);
    }

    @RolesAllowed(value = {"ADMIN"})
    @PostMapping("/teacher")
    ResponseEntity<AccountViewDTO> addTeacher(
            @RequestBody @Valid AccountTeacherDTO accountTeacherDTO) {
        AccountViewDTO teacher = accountService.saveTeacherAccount(accountTeacherDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teacher);
    }

    @RolesAllowed(value = {"ADMIN"})
    @GetMapping("/{role}={id}")
    ResponseEntity<AccountViewDTO> getAccountById(
            @PathVariable Role role,
            @PathVariable int id) {
        AccountViewDTO accountViewDTO = accountService.getAccountById(role,id);
        return ResponseEntity.ok(accountViewDTO);
    }

    @RolesAllowed(value = {"ADMIN"})
    @DeleteMapping("/{role}={id}")
    ResponseEntity<AccountViewDTO> deleteAccountById(
            @PathVariable Role role,
            @PathVariable int id) {
        AccountViewDTO accountViewDTO = accountService.deleteAccountById(role,id);
        return ResponseEntity.ok(accountViewDTO);
    }

    //TODO: get id from jwt token - user should change only his own password
    @PermitAll
    @PutMapping("/{role}={id}")
    ResponseEntity<AccountViewDTO> changeAccountPassword(
            @PathVariable Role role,
            @PathVariable int id,
            @RequestParam String oldPassword,
            @RequestParam @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{6,25}$") String newPassword) {
        AccountViewDTO accountViewDTO = accountService.changeAccountPassword(role,id,oldPassword,newPassword);
        return ResponseEntity.ok(accountViewDTO);
    }

}
