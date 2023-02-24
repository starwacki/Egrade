package com.github.starwacki.components.account.service.generator;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.model.Student;
import com.github.starwacki.components.student.model.SchoolClass;
import com.github.starwacki.components.account.model.Role;
import com.github.starwacki.repositories.SchoolClassRepository;
import com.github.starwacki.repositories.StudentRepository;
import com.github.starwacki.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentManuallyGenerator extends AccountGenerator {


    public StudentManuallyGenerator(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository, TeacherRepository teacherRepository) {
        super(studentRepository, schoolClassRepository, teacherRepository);
    }

    public Student generateStudentAccount(AccountStudentDTO studentDTO) {
        return Student.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .role(Role.STUDENT)
                .schoolClass(getSchoolClass(studentDTO))
                .username(generateAccountUsername(studentDTO.firstname(),studentDTO.lastname(),getLastStudentId()))
                .password(generateFirstPassword())
                .build();
    }

    private SchoolClass getSchoolClass(AccountStudentDTO studentDTO) {
        return schoolClassRepository.findByNameAndClassYear(studentDTO.className(), studentDTO.year())
                .orElse(getNewSchoolClass(studentDTO));

    }

    private long getLastStudentId() {
        return (studentRepository.count() + 1);
    }

    private SchoolClass getNewSchoolClass(AccountStudentDTO studentDTO) {
        return new SchoolClass(studentDTO.className(), studentDTO.year());
    }

    @Override
    protected String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname + "." + lastname + getStudentAccountIdentity()+id;
    }

    private String getStudentAccountIdentity() {
        return "STU";
    }
}
