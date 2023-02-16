package com.github.starwacki.student.controller;

import com.github.starwacki.student.dto.GradeDTO;
import com.github.starwacki.student.dto.StudentDTO;
import com.github.starwacki.student.dto.StudentGradesDTO;
import com.github.starwacki.student.service.StudentGradeService;
import com.github.starwacki.student.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@AllArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;
    private final StudentGradeService studentGradeService;

    @GetMapping("/students/{className}/{classYear}")
    ResponseEntity<List<StudentDTO>> getAllStudentsFromClass(
            @PathVariable  String className,
            @PathVariable  int classYear) {
        List<StudentDTO> accounts = studentService.getAllStudentsFromClass(className,classYear);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/student={id}/class")
    ResponseEntity<StudentDTO> changeStudentClass(
            @PathVariable int id,
            @RequestParam @Pattern(regexp = "^[1-9][A-Z]$") String className,
            @RequestParam @Min(2020) @Max(2040) int year) {
        StudentDTO accountViewDTO = studentService.changeStudentClass(id,className,year);
        return ResponseEntity.ok(accountViewDTO);
    }


    @PostMapping("/student={id}/grade")
    ResponseEntity<GradeDTO> addGradeToStudent(
            @PathVariable int id,
            @RequestBody @Valid GradeDTO gradeDTO) {
        return ResponseEntity.ok(studentGradeService.addGradeToStudent(gradeDTO,id));
    }

    @GetMapping("/student={id}/grade={gradeID}")
    ResponseEntity<GradeDTO> getStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID) {
        return ResponseEntity.ok(studentGradeService.getOneGrade(id,gradeID));
    }

    @PutMapping("/student={id}/grade={gradeID}")
    ResponseEntity<GradeDTO> updateStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID,
            @RequestBody @Valid GradeDTO gradeDTO) {
        GradeDTO grade = studentGradeService.updateGrade(id,gradeID, gradeDTO);
        return ResponseEntity.ok(grade);
    }


    @GetMapping("/student={id}/grades")
    ResponseEntity<StudentGradesDTO> getStudentGrades(@PathVariable int id) {
        StudentGradesDTO grades = studentGradeService.getAllStudentsGrade(id);
        return ResponseEntity.ok(grades);
    }


    @GetMapping("/grade/student={id}/subject={subjectID}")
    ResponseEntity<StudentGradesDTO> getStudentSubjectGrades(
            @PathVariable int id,
            @PathVariable int subjectID) {
        return ResponseEntity.ok(studentGradeService.getOneSubjectGrade(id,subjectID));
    }






}
