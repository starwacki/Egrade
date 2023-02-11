package com.github.starwacki.controller;


import com.github.starwacki.model.account.Student;
import com.github.starwacki.service.account_generator_service.AccountCSVGenerator;
import com.github.starwacki.service.account_generator_service.StudentDTO;
import com.github.starwacki.service.account_generator_service.AccountManuallyGenerator;
import com.github.starwacki.service.account_generator_service.StudentsCsvDTO;
import com.github.starwacki.service.account_generator_service.exception.WrongFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentController {

    private final AccountManuallyGenerator studentService;

    private final AccountCSVGenerator accountCSVGenerator;


    @PostMapping("/student")
    ResponseEntity<Student> addStudent(@RequestBody StudentDTO studentDTO) {
        Student student = studentService.generateStudentAndParentAccount(studentDTO);
        return ResponseEntity.created(URI.create("/" + student.getId())).body(student);
    }

    @GetMapping("/student/{id}")
    ResponseEntity<Student> getStudent(@PathVariable int id) {
        return studentService.getStudent(id)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/students")
    ResponseEntity<List<Student>> addTest(@RequestBody StudentsCsvDTO studentsCsvDTO) throws WrongFileException, IOException {
      List<Student> list = accountCSVGenerator.generateStudents(studentsCsvDTO);
      return ResponseEntity.ok(list);
    }








}
