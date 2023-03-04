package com.github.starwacki.components.teacher.controller;


import com.github.starwacki.components.teacher.dto.SchoolClassDTO;
import com.github.starwacki.components.teacher.dto.TeacherDTO;
import com.github.starwacki.components.teacher.service.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@Validated
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/teacher={id}/classes")
    public ResponseEntity<List<SchoolClassDTO>> getTeacherClasses(@PathVariable int id) {
        List<SchoolClassDTO> teacherClasses = teacherService.getTeacherClasses(id);
        return ResponseEntity.ok(teacherClasses);
    }

    @PutMapping("/teacher={id}/classes")
    public ResponseEntity<?> addSchoolClassToTeacher(
            @PathVariable int id,
            @RequestBody @Valid SchoolClassDTO schoolClassDTO) {
            teacherService.addSchoolClassToTeacher(id,schoolClassDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherDTO>> getAllTeachersInformation() {
        List<TeacherDTO> teacherDTO = teacherService.getAllTeachers();
        return ResponseEntity.ok(teacherDTO);
    }


}
