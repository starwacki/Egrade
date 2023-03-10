package com.github.starwacki.components.account.service.generator;

import com.github.starwacki.global.model.account.Parent;
import com.github.starwacki.global.model.account.Role;
import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.global.repositories.SchoolClassRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import com.github.starwacki.global.security.EgradePasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ParentManuallyGeneratorStrategy extends AccountGeneratorStrategy {


    protected ParentManuallyGeneratorStrategy(StudentRepository studentRepository,
                                              SchoolClassRepository schoolClassRepository,
                                              TeacherRepository teacherRepository) {
        super(studentRepository, schoolClassRepository, teacherRepository);
    }

    private long getLastStudentId() {
        return (studentRepository.count() + 1);
    }

    @Override
    public Parent createAccount(Record dto) {
        AccountStudentDTO studentDTO = (AccountStudentDTO) dto;
        return Parent.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .username(generateAccountUsername(studentDTO.firstname(),studentDTO.lastname(),getLastStudentId()))
                .password(generateFirstPassword())
                .role(Role.PARENT)
                .phoneNumber(studentDTO.parentPhoneNumber())
                .build();
    }

    @Override
    protected String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname + "." + lastname + "RO"+id;
    }

}
