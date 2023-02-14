package com.github.starwacki.account.service.generator;

import com.github.starwacki.account.model.Parent;
import com.github.starwacki.account.model.Role;
import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import com.github.starwacki.account.dto.AccountStudentDTO;
import org.springframework.stereotype.Service;

@Service
public class ParentManuallyGenerator extends AccountGenerator {

    protected ParentManuallyGenerator(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository) {
        super(studentRepository, schoolClassRepository);
    }

    public Parent generateParentAccount(AccountStudentDTO studentDTO) {
        return Parent.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .username(generateParentUsername(studentDTO))
                .password(generateFirstPassword())
                .role(Role.PARENT)
                .phoneNumber(studentDTO.parentPhoneNumber())
                .build();
    }

    private String generateParentUsername(AccountStudentDTO studentDTO) {
        return studentDTO.firstname() + "." + studentDTO.lastname() + getLastStudentId() + getParentAccountIdentity();
    }

    private String getParentAccountIdentity() {
        return "RO";
    }
}
