package com.github.starwacki.components.grades;

import com.github.starwacki.components.grades.dto.GradeDTO;
import com.github.starwacki.components.grades.dto.GradeViewDTO;
import com.github.starwacki.components.grades.dto.SubjectDTO;
import jakarta.annotation.security.PermitAll;
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
@RequestMapping("/grades")
class GradesController implements GradesControllerOperations {

    private final GradeFacade gradeFacade;

    @Secured(value = {"ADMIN","TEACHER"})
    @PostMapping("/grade")
    public ResponseEntity<GradeDTO> addGradeToStudent(
            @RequestBody GradeDTO gradeDTO) {
        GradeDTO grade = gradeFacade.addGradeToStudent(gradeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(grade);
    }

    @PermitAll
    @GetMapping("/student={studentID}/{gradeID}")
    public ResponseEntity<GradeViewDTO> getStudentGrade(
            @PathVariable int studentID,
            @PathVariable int gradeID) {
        GradeViewDTO gradeViewDTO = gradeFacade.getOneGrade(studentID,gradeID);
        return ResponseEntity.ok(gradeViewDTO);
    }

    @Secured(value = {"ADMIN","TEACHER"})
    @PutMapping("/student={studentID}/{gradeID}")
    public ResponseEntity<GradeDTO> updateStudentGrade(
            @PathVariable int studentID,
            @PathVariable int gradeID,
            @RequestBody  GradeDTO gradeDTO) {
        GradeDTO grade = gradeFacade.updateGrade(studentID,gradeID, gradeDTO);
        return ResponseEntity.ok(grade);
    }

    @Secured(value = {"ADMIN","TEACHER"})
    @DeleteMapping("/student={studentID}/{gradeID}")
    public ResponseEntity<GradeDTO> deleteStudentGrade(
            @PathVariable int studentID,
            @PathVariable int gradeID) {
        GradeDTO grade = gradeFacade.deleteStudentGrade(studentID,gradeID);
        return ResponseEntity.ok(grade);
    }

    @PermitAll
    @GetMapping("/student={studentID}")
    public ResponseEntity<List<SubjectDTO>> getStudentGrades(@PathVariable int studentID) {
        List<SubjectDTO> grades = gradeFacade.getAllGradesByStudentID(studentID);
        return ResponseEntity.ok(grades);
    }

    @PermitAll
    @GetMapping("/student={studentID}/subject={subjectID}")
    public ResponseEntity<SubjectDTO> getStudentSubjectGrades(
            @PathVariable int studentID,
            @PathVariable int subjectID) {
        return ResponseEntity.ok(gradeFacade.getOneSubjectGrades(studentID,subjectID));
    }
}
