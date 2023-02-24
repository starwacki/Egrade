package com.github.starwacki.components.account.controller;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.model.Role;
import com.github.starwacki.components.account.service.AccountService;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.components.account.exceptions.exception.WrongFileException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/account/student")
    ResponseEntity<AccountViewDTO> addStudent(@RequestBody @Valid AccountStudentDTO studentDTO) {
        AccountViewDTO student = accountService.saveStudentAndParentAccount(studentDTO);
        return ResponseEntity
                .created(URI.create("http://localhost:8080/account/STUDENT=" + student.id()))
                .body(student);
    }

    @PostMapping("/account/students")
    ResponseEntity<List<AccountViewDTO>> addStudentsFromCSVFile(
            @RequestParam @NotBlank String pathname) throws WrongFileException {
      List<AccountViewDTO> list = accountService.saveStudentsAndParentsFromFile(pathname);
      return ResponseEntity
              .status(HttpStatus.CREATED)
              .body(list);
    }

    @PostMapping("/account/teacher")
    ResponseEntity<AccountViewDTO> addTeacher(@RequestBody @Valid AccountTeacherDTO accountTeacherDTO) {
        AccountViewDTO teacher = accountService.saveTeacherAccount(accountTeacherDTO);
        return ResponseEntity
                .created(URI.create("http://localhost:8080/account/TEACHER=" + teacher.id()))
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
            @RequestParam @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$") String newPassword) {
        AccountViewDTO accountViewDTO = accountService.changeAccountPassword(role,id,oldPassword,newPassword);
        return ResponseEntity.ok(accountViewDTO);
    }

}
