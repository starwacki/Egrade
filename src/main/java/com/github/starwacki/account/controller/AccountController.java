package com.github.starwacki.account.controller;


import com.github.starwacki.account.dto.AccountTeacherDTO;
import com.github.starwacki.account.model.Role;
import com.github.starwacki.account.service.AccountService;
import com.github.starwacki.account.dto.AccountStudentDTO;
import com.github.starwacki.account.dto.AccountViewDTO;
import com.github.starwacki.account.exception.WrongFileException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
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
        return ResponseEntity.created(URI.create("http://localhost:8080/account/student=" + student.id())).body(student);
    }

    @GetMapping("/account/student={id}")
    ResponseEntity<AccountViewDTO> getStudentAccountById(@PathVariable int id) {
        AccountViewDTO accountViewDTO = accountService.getAccountById(Role.STUDENT,id);
        return ResponseEntity.ok(accountViewDTO);
    }

    @DeleteMapping("/account/student={id}")
    ResponseEntity<AccountViewDTO> deleteStudentById(@PathVariable int id) {
        AccountViewDTO accountViewDTO = accountService.deleteStudentAccountById(id);
        return ResponseEntity.ok(accountViewDTO);
    }

    @PutMapping("/account/student={id}/password")
    ResponseEntity<AccountViewDTO> changeStudentPassword(
            @PathVariable int id,
            @RequestParam String oldPassword,
            @RequestParam @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$") String newPassword) {
        AccountViewDTO accountViewDTO = accountService.changeAccountPassword(Role.STUDENT,id,oldPassword,newPassword);
        return ResponseEntity.ok(accountViewDTO);
    }

    @PutMapping("/account/student={id}/class")
    ResponseEntity<AccountViewDTO> changeStudentClass(
            @PathVariable int id,
            @RequestParam @Pattern(regexp = "^[1-9][A-Z]$") String className,
            @RequestParam @Min(2020) @Max(2040) int year) {
        AccountViewDTO accountViewDTO = accountService.changeStudentClass(id,className,year);
        return ResponseEntity.ok(accountViewDTO);
    }

    @PostMapping("/account/students")
    ResponseEntity<List<AccountViewDTO>> addStudentsFromCSVFile(
            @RequestParam @NotBlank String pathname) throws WrongFileException, IOException {
      List<AccountViewDTO> list = accountService.saveStudentsAndParentsFromFile(pathname);
      return ResponseEntity.ok(list);
    }

    @GetMapping("/accounts/students/{className}/{classYear}")
    ResponseEntity<List<AccountViewDTO>> getAllStudentsFromClass(
            @PathVariable  String className,
            @PathVariable  int classYear) {
        List<AccountViewDTO> accounts = accountService.getAllStudentsFromClass(className,classYear);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/account/teacher")
    ResponseEntity<AccountViewDTO> addTeacher(@RequestBody @Valid AccountTeacherDTO accountTeacherDTO) {
        AccountViewDTO teacher = accountService.saveTeacherAccount(accountTeacherDTO);
        return ResponseEntity.created(URI.create("http://localhost:8080/account/teacher=" + teacher.id())).body(teacher);
    }

    @GetMapping("/account/teacher={id}")
    ResponseEntity<AccountViewDTO> getTeacherAccountById(@PathVariable int id) {
        AccountViewDTO teacher = accountService.getAccountById(Role.TEACHER,id);
        return ResponseEntity.ok(teacher);
    }

    @PutMapping("/account/teacher={id}")
    ResponseEntity<AccountViewDTO> changeTeacherPassword(
            @PathVariable int id,
            @RequestParam String oldPassword,
            @RequestParam @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$") String newPassword) {
        AccountViewDTO accountViewDTO = accountService.changeAccountPassword(Role.TEACHER,id,oldPassword,newPassword);
        return ResponseEntity.ok(accountViewDTO);
    }











}
