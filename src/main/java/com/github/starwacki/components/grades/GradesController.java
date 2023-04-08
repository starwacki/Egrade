package com.github.starwacki.components.grades;

import com.github.starwacki.components.grades.dto.GradeRequestDTO;
import com.github.starwacki.components.grades.dto.GradeResponeDTO;
import com.github.starwacki.components.grades.dto.SubjectResponseDTO;
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
    public ResponseEntity<GradeRequestDTO> addGradeToStudent(
            @RequestBody GradeRequestDTO gradeRequestDTO) {
        GradeRequestDTO grade = gradeFacade.addGradeToStudent(gradeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(grade);
    }

    @PermitAll
    @GetMapping("/student={studentID}/{gradeID}")
    public ResponseEntity<GradeResponeDTO> getStudentGrade(
            @PathVariable int studentID,
            @PathVariable int gradeID) {
        GradeResponeDTO gradeResponeDTO = gradeFacade.getOneGrade(studentID,gradeID);
        return ResponseEntity.ok(gradeResponeDTO);
    }

    @Secured(value = {"ADMIN","TEACHER"})
    @PutMapping("/student={studentID}/{gradeID}")
    public ResponseEntity<GradeRequestDTO> updateStudentGrade(
            @PathVariable int studentID,
            @PathVariable int gradeID,
            @RequestBody GradeRequestDTO gradeRequestDTO) {
        GradeRequestDTO grade = gradeFacade.updateGrade(studentID,gradeID, gradeRequestDTO);
        return ResponseEntity.ok(grade);
    }

    @Secured(value = {"ADMIN","TEACHER"})
    @DeleteMapping("/student={studentID}/{gradeID}")
    public ResponseEntity<GradeRequestDTO> deleteStudentGrade(
            @PathVariable int studentID,
            @PathVariable int gradeID) {
        GradeRequestDTO grade = gradeFacade.deleteStudentGrade(studentID,gradeID);
        return ResponseEntity.ok(grade);
    }

    @PermitAll
    @GetMapping("/student={studentID}")
    public ResponseEntity<List<SubjectResponseDTO>> getStudentGrades(@PathVariable int studentID) {
        List<SubjectResponseDTO> grades = gradeFacade.getAllGradesByStudentID(studentID);
        return ResponseEntity.ok(grades);
    }

    @PermitAll
    @GetMapping("/student={studentID}/subject={subjectID}")
    public ResponseEntity<SubjectResponseDTO> getStudentSubjectGrades(
            @PathVariable int studentID,
            @PathVariable int subjectID) {
        return ResponseEntity.ok(gradeFacade.getOneSubjectGrades(studentID,subjectID));
    }
}
