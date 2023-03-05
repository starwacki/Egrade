package com.github.starwacki.components.account.controller;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.global.model.account.Role;
import com.github.starwacki.components.account.service.AccountService;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequiredArgsConstructor
@Validated
@Controller
public class AccountController {

    private final AccountService accountService;

    @PermitAll
    @PostMapping("/account/student")
    ResponseEntity<AccountViewDTO> addStudent(@RequestBody @Valid AccountStudentDTO studentDTO) {
        AccountViewDTO student = accountService.saveStudentAndParentAccount(studentDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(student);
    }

    @PostMapping("/account/students")
    ResponseEntity<List<AccountViewDTO>> addStudentsFromCSVFile(
            @RequestParam @NotBlank String pathname) {
      List<AccountViewDTO> list = accountService.saveStudentsAndParentsFromFile(pathname);
      return ResponseEntity
              .status(HttpStatus.CREATED)
              .body(list);
    }

    @PostMapping("/account/teacher")
    ResponseEntity<AccountViewDTO> addTeacher(@RequestBody @Valid AccountTeacherDTO accountTeacherDTO) {
        AccountViewDTO teacher = accountService.saveTeacherAccount(accountTeacherDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teacher);
    }

    @GetMapping("/account/{role}={id}")
    ResponseEntity<AccountViewDTO> getAccountById(
            @PathVariable Role role,
            @PathVariable int id) {
        AccountViewDTO accountViewDTO = accountService.getAccountById(role,id);
        return ResponseEntity.ok(accountViewDTO);
    }

    @DeleteMapping("/account/{role}={id}")
    ResponseEntity<AccountViewDTO> deleteAccountById(
            @PathVariable Role role,
            @PathVariable int id) {
        AccountViewDTO accountViewDTO = accountService.deleteAccountById(role,id);
        return ResponseEntity.ok(accountViewDTO);
    }

    @PutMapping("/account/{role}={id}")
    ResponseEntity<AccountViewDTO> changeAccountPassword(
            @PathVariable Role role,
            @PathVariable int id,
            @RequestParam String oldPassword,
            @RequestParam @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{6,25}$") String newPassword) {
        AccountViewDTO accountViewDTO = accountService.changeAccountPassword(role,id,oldPassword,newPassword);
        return ResponseEntity.ok(accountViewDTO);
    }

}
