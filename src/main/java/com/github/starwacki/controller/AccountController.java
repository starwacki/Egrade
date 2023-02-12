package com.github.starwacki.controller;


import com.github.starwacki.model.account.Student;
import com.github.starwacki.service.account_generator_service.AccountGeneratorService;
import com.github.starwacki.service.account_generator_service.dto.AccountStudentDTO;
import com.github.starwacki.service.account_generator_service.exception.WrongFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {


    private final AccountGeneratorService accountGeneratorService;


    @PostMapping("/account/student")
    ResponseEntity<Student> addStudent(@RequestBody AccountStudentDTO studentDTO) {
        Student student =accountGeneratorService.saveStudentAndParentAccount(studentDTO);
        return ResponseEntity.created(URI.create("/" + student.getId())).body(student);
    }

    @PostMapping("/account/students")
    ResponseEntity<List<Student>> addStudentsFromCSVFile(@RequestParam String pathname) throws WrongFileException, IOException {
      List<Student> list = accountGeneratorService.saveStudentsAndParentsFromFile(pathname);
      return ResponseEntity.ok(list);
    }









}
