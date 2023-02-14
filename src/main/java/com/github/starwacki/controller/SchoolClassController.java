package com.github.starwacki.controller;

import com.github.starwacki.service.school_class_service.SchoolClassDTO;
import com.github.starwacki.service.school_class_service.SchoolClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SchoolClassController {


    private final SchoolClassService schoolClassService;

    @GetMapping("/classes")
    ResponseEntity<List<SchoolClassDTO>> schoolClasses() {
        return ResponseEntity.ok(schoolClassService.getAllSchoolClasses());
    }



}
