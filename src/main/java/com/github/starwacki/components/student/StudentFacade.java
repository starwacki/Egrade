package com.github.starwacki.components.student;


import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.components.student.exceptions.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentFacade {

    private final StudentRepository studentRepository;

    List<StudentDTO> getAllStudentsFromClass(String className, int classYear) {
        return studentRepository
                .findAllBySchoolClassNameAndSchoolClassYear(className,classYear)
                .stream()
                .map(student -> StudentMapper.mapStudentToStudentDTO(student))
                .toList();
    }

    void changeStudentClass(int id, String className, int year) {
       studentRepository
                .findById(id)
                .map(student -> setSchoolClass(student,className,year))
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    private Student setSchoolClass(Student student, String className, int year) {
        student.setSchoolClassName(className);
        student.setSchoolClassYear(year);
        studentRepository.save(student);
        return student;
    }





}
