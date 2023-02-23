package com.github.starwacki.student.controller;

import com.github.starwacki.student.dto.GradeDTO;
import com.github.starwacki.student.dto.GradeViewDTO;
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
    ResponseEntity<?> changeStudentClass(
            @PathVariable int id,
            @RequestParam @Pattern(regexp = "^[1-9][A-Z]$") String className,
            @RequestParam @Min(2020) @Max(2040) int year) {
        studentService.changeStudentClass(id,className,year);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/student={id}/grade")
    ResponseEntity<GradeDTO> addGradeToStudent(
            @PathVariable int id,
            @RequestBody @Valid GradeDTO gradeDTO) {
        GradeDTO grade = studentGradeService.addGradeToStudent(gradeDTO,id);
        return ResponseEntity.ok(grade);
    }

    @GetMapping("/student={id}/grade={gradeID}")
    ResponseEntity<GradeViewDTO> getStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID) {
        GradeViewDTO gradeViewDTO = studentGradeService.getOneGrade(id,gradeID);
        return ResponseEntity.ok(gradeViewDTO);
    }

    @PutMapping("/student={id}/grade={gradeID}")
    ResponseEntity<GradeDTO> updateStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID,
            @RequestBody @Valid GradeDTO gradeDTO) {
        GradeDTO grade = studentGradeService.updateGrade(id,gradeID, gradeDTO);
        return ResponseEntity.ok(grade);
    }

    @DeleteMapping("/student={id}/grade={gradeID}")
    ResponseEntity<GradeDTO> deleteStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID) {
        GradeDTO grade = studentGradeService.deleteStudentGrade(id,gradeID);
        return ResponseEntity.ok(grade);
    }

    @GetMapping("/student={id}/grades")
    ResponseEntity<StudentGradesDTO> getStudentGrades(@PathVariable int id) {
        StudentGradesDTO grades = studentGradeService.getAllSubjectGradesByStudentID(id);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/student={id}/grades/subject={subjectID}")
    ResponseEntity<StudentGradesDTO> getStudentSubjectGrades(
            @PathVariable int id,
            @PathVariable int subjectID) {
        return ResponseEntity.ok(studentGradeService.getOneSubjectGrades(id,subjectID));
    }






}
