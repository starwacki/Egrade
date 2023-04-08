package com.github.starwacki.components.student;

import com.github.starwacki.components.grades.GradeFacade;
import com.github.starwacki.components.student.dto.StudentDTO;
import lombok.AllArgsConstructor;
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
class StudentController implements StudentControllerOperations
{

    private final StudentFacade studentFacade;

    @Secured(value = {"ADMIN","TEACHER"})
    @GetMapping("/class={className}&year={classYear}")
    public ResponseEntity<List<StudentDTO>> getAllStudentsFromClass(
            @PathVariable  String className,
            @PathVariable int classYear) {
        List<StudentDTO> accounts = studentFacade.getAllStudentsFromClass(className,classYear);
        return ResponseEntity.ok(accounts);
    }


    @Secured(value = {"ADMIN","TEACHER"})
    @PutMapping("/id={id}/class")
    public ResponseEntity<?> changeStudentClass(
            @PathVariable int id,
            @RequestParam String className,
            @RequestParam int classYear) {
        studentFacade.changeStudentClass(id,className,classYear);
        return ResponseEntity.noContent().build();
    }



}
