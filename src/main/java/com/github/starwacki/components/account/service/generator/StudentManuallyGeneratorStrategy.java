package com.github.starwacki.components.account.service.generator;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.global.model.account.Student;
import com.github.starwacki.global.model.school_class.SchoolClass;
import com.github.starwacki.global.model.account.Role;
import com.github.starwacki.global.repositories.SchoolClassRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import com.github.starwacki.global.security.EgradePasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentManuallyGeneratorStrategy extends AccountGeneratorStrategy {


    public StudentManuallyGeneratorStrategy(StudentRepository studentRepository,
                                            SchoolClassRepository schoolClassRepository,
                                            TeacherRepository teacherRepository) {
        super(studentRepository, schoolClassRepository, teacherRepository);
    }

    @Override
    public Student createAccount(Record dto) {
        AccountStudentDTO studentDTO = (AccountStudentDTO) dto;
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
        return schoolClassRepository.findSchoolClassByNameAndAndClassYear(studentDTO.className(), studentDTO.year())
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
