package com.github.starwacki.components.student.service;


import com.github.starwacki.components.account.AccountStudent;
import com.github.starwacki.components.student.exceptions.StudentNotFoundException;
import com.github.starwacki.common.repositories.SchoolClassRepository;
import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.components.student.mapper.StudentMapper;
import com.github.starwacki.common.model.school_class.SchoolClass;
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

    public void changeStudentClass(int id, String className, int year) {
       studentRepository
                .findById(id)
                .map(student -> setSchoolClass(student,className,year))
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    private AccountStudent setSchoolClass(AccountStudent accountStudent, String className, int year) {
        accountStudent.setSchoolClass(getSchoolClass(className,year));
        studentRepository.save(accountStudent);
        return accountStudent;
    }

    private SchoolClass getSchoolClass(String className, int year) {
        return schoolClassRepository
                .findSchoolClassByNameAndAndClassYear(className,year)
                .orElse(createNewSchoolClass(className,year));
    }

    private SchoolClass createNewSchoolClass(String className, int year) {
        return new SchoolClass(className,year);
    }


}
