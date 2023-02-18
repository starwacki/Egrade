package com.github.starwacki.student.service;


import com.github.starwacki.account.model.Student;
import com.github.starwacki.repositories.SchoolClassRepository;
import com.github.starwacki.repositories.StudentRepository;
import com.github.starwacki.student.dto.StudentDTO;
import com.github.starwacki.student.exceptions.StudentNotFoundException;
import com.github.starwacki.student.mapper.StudentMapper;
import com.github.starwacki.student.model.SchoolClass;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;

    public List<StudentDTO> getAllStudentsFromClass(String className, int classYear) {
        return studentRepository
                .findAllBySchoolClassNameAndSchoolClassClassYear(className,classYear)
                .stream()
                .map(student -> StudentMapper.mapStudentToStudentDTO(student))
                .toList();
    }

    public StudentDTO changeStudentClass(int id, String className, int year) {
        return studentRepository
                .findById(id)
                .map(student -> setSchoolClass(student,className,year))
                .map(student -> StudentMapper.mapStudentToStudentDTO(student))
                .orElseThrow(() -> new StudentNotFoundException());
    }

    private Student setSchoolClass(Student student, String className, int year) {
        student.setSchoolClass(getSchoolClass(className,year));
        studentRepository.save(student);
        return student;
    }

    private SchoolClass getSchoolClass(String className, int year) {
        return schoolClassRepository
                .findByNameAndClassYear(className,year)
                .orElse(createNewSchoolClass(className,year));
    }

    private SchoolClass createNewSchoolClass(String className, int year) {
        return new SchoolClass(className,year);
    }


}
