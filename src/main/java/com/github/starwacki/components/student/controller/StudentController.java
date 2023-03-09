package com.github.starwacki.components.student.controller;

import com.github.starwacki.components.student.dto.GradeDTO;
import com.github.starwacki.components.student.dto.GradeViewDTO;
import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.components.student.dto.StudentGradesDTO;
import com.github.starwacki.components.student.service.StudentGradeService;
import com.github.starwacki.components.student.service.StudentService;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@Validated
@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final StudentGradeService studentGradeService;

    @RolesAllowed(value = {"ADMIN","TEACHER"})
    @GetMapping("/class={className}&year={classYear}")
    ResponseEntity<List<StudentDTO>> getAllStudentsFromClass(
            @PathVariable @Pattern(regexp = "^[1-9][A-Z]$")  String className,
            @PathVariable @Min(2020) @Max(2040)  int classYear) {
        List<StudentDTO> accounts = studentService.getAllStudentsFromClass(className,classYear);
        return ResponseEntity.ok(accounts);
    }


    @RolesAllowed(value = {"ADMIN","TEACHER"})
    @PutMapping("/id={id}/class")
    ResponseEntity<?> changeStudentClass(
            @PathVariable int id,
            @RequestParam @Pattern(regexp = "^[1-9][A-Z]$") String className,
            @RequestParam @Min(2020) @Max(2040) int classYear) {
        studentService.changeStudentClass(id,className,classYear);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed(value = {"ADMIN","TEACHER"})
    @PostMapping("/id={id}/grade")
    ResponseEntity<GradeDTO> addGradeToStudent(
            @PathVariable int id,
            @RequestBody @Valid GradeDTO gradeDTO) {
        GradeDTO grade = studentGradeService.addGradeToStudent(gradeDTO,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(grade);
    }

    @PermitAll
    @GetMapping("/id={id}/grade={gradeID}")
    ResponseEntity<GradeViewDTO> getStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID) {
        GradeViewDTO gradeViewDTO = studentGradeService.getOneGrade(id,gradeID);
        return ResponseEntity.ok(gradeViewDTO);
    }

    @RolesAllowed(value = {"ADMIN","TEACHER"})
    @PutMapping("/id={id}/grade={gradeID}")
    ResponseEntity<GradeDTO> updateStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID,
            @RequestBody @Valid GradeDTO gradeDTO) {
        GradeDTO grade = studentGradeService.updateGrade(id,gradeID, gradeDTO);
        return ResponseEntity.ok(grade);
    }

    @RolesAllowed(value = {"ADMIN","TEACHER"})
    @DeleteMapping("/id={id}/grade={gradeID}")
    ResponseEntity<GradeDTO> deleteStudentGrade(
            @PathVariable int id,
            @PathVariable int gradeID) {
        GradeDTO grade = studentGradeService.deleteStudentGrade(id,gradeID);
        return ResponseEntity.ok(grade);
    }

    @RolesAllowed(value = "ADMIN")
    @GetMapping("/id={id}/grades")
    ResponseEntity<StudentGradesDTO> getStudentGrades(@PathVariable int id) {
        StudentGradesDTO grades = studentGradeService.getAllSubjectGradesByStudentID(id);
        return ResponseEntity.ok(grades);
    }

    @PermitAll
    @GetMapping("/id={id}/grades/subject={subjectID}")
    ResponseEntity<StudentGradesDTO> getStudentSubjectGrades(
            @PathVariable int id,
            @PathVariable int subjectID) {
        return ResponseEntity.ok(studentGradeService.getOneSubjectGrades(id,subjectID));
    }






}
