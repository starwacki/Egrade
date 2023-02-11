package com.github.starwacki.service.school_class_service;


import com.github.starwacki.repository.SchoolClassRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;


    public List<SchoolClassDTO> getAllSchoolClasses() {
        return schoolClassRepository.findAll().stream()
                .map(schoolClass ->
                         SchoolClassDTO.builder()
                        .name(schoolClass.getName())
                        .classYear(schoolClass.getClassYear())
                        .build())
                .toList();

    }

    public void test() {

        }
    }





