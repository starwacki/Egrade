package com.github.starwacki.account.controller;


import com.github.starwacki.account.dto.AccountTeacherDTO;
import com.github.starwacki.account.model.Student;
import com.github.starwacki.account.service.AccountService;
import com.github.starwacki.account.dto.AccountStudentDTO;
import com.github.starwacki.account.dto.AccountViewDTO;
import com.github.starwacki.account.exception.WrongFileException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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
    ResponseEntity<Student> addStudent(@RequestBody AccountStudentDTO studentDTO) {
        Student student = accountService.saveStudentAndParentAccount(studentDTO);
        return ResponseEntity.created(URI.create("/fdf" + student.getId())).body(student);
    }

    @GetMapping("/account/student={id}")
    ResponseEntity<AccountViewDTO> getStudentAccountById(@PathVariable int id) {
        AccountViewDTO accountViewDTO = accountService.getStudentAccount(id);
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
            @RequestParam @Valid @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$") String newPassword) {
        AccountViewDTO accountViewDTO = accountService.changeStudentPassword(id,oldPassword,newPassword);
        return ResponseEntity.ok(accountViewDTO);
    }

    @PutMapping("/account/student={id}/class")
    ResponseEntity<AccountViewDTO> changeStudentClass(
            @PathVariable int id,
            @RequestParam String className,
            @RequestParam int year) {
        AccountViewDTO accountViewDTO = accountService.changeStudentClass(id,className,year);
        return ResponseEntity.ok(accountViewDTO);
    }

    @PostMapping("/account/students")
    ResponseEntity<List<Student>> addStudentsFromCSVFile(
            @RequestParam String pathname) throws WrongFileException, IOException {
      List<Student> list = accountService.saveStudentsAndParentsFromFile(pathname);
      return ResponseEntity.ok(list);
    }

    @GetMapping("/accounts/students/{className}/{classYear}")
    ResponseEntity<List<AccountViewDTO>> getAllStudentsFromClass(
            @PathVariable String className,
            @PathVariable int classYear) {
        List<AccountViewDTO> accounts = accountService.getAllStudentsFromClass(className,classYear);
        return ResponseEntity.ok(accounts);
    }









}
