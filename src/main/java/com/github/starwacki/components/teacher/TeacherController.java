package com.github.starwacki.components.teacher;

import com.github.starwacki.components.teacher.dto.TeacherResponseDTO;
import com.github.starwacki.components.teacher.dto.TeacherSchoolClassDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@AllArgsConstructor
@Validated
@RequestMapping("/teacher")
class TeacherController  implements TeacherOperations
 {

    private final TeacherFacade teacherFacade;

    @PermitAll
    @GetMapping("/id={id}/classes")
    public ResponseEntity<List<TeacherSchoolClassDTO>> getTeacherClasses(@PathVariable int id) {
        List<TeacherSchoolClassDTO> teacherClasses = teacherFacade.getTeacherClasses(id);
        return ResponseEntity.ok(teacherClasses);
    }

    @Secured(value = {"ADMIN"})
    @PutMapping("/id={id}/classes")
    public ResponseEntity<?> addSchoolClassToTeacher(
            @PathVariable int id,
            @RequestBody @Valid TeacherSchoolClassDTO schoolClassDTO) {
            teacherFacade.addSchoolClassToTeacher(id,schoolClassDTO);
        return ResponseEntity.noContent().build();
    }

    @PermitAll
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachersInformation() {
        List<TeacherResponseDTO> teacherDTO = teacherFacade.getAllTeachers();
        return ResponseEntity.ok(teacherDTO);
    }


}
