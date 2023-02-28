package com.github.starwacki.components.account.service.generator;

import com.github.starwacki.components.account.model.Parent;
import com.github.starwacki.components.account.model.Role;
import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.global.repositories.SchoolClassRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class ParentManuallyGenerator extends AccountGenerator {


    protected ParentManuallyGenerator(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository, TeacherRepository teacherRepository) {
        super(studentRepository, schoolClassRepository, teacherRepository);
    }

    public Parent generateParentAccount(AccountStudentDTO studentDTO) {
        return Parent.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .username(generateAccountUsername(studentDTO.firstname(),studentDTO.lastname(),getLastStudentId()))
                .password(generateFirstPassword())
                .role(Role.PARENT)
                .phoneNumber(studentDTO.parentPhoneNumber())
                .build();
    }

    private long getLastStudentId() {
        return (studentRepository.count() + 1);
    }
    @Override
    protected String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname + "." + lastname + getParentAccountIdentity()+id;
    }

    private String getParentAccountIdentity() {
        return "RO";
    }

}
