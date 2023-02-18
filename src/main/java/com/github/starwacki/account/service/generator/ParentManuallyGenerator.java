package com.github.starwacki.account.service.generator;

import com.github.starwacki.account.model.Parent;
import com.github.starwacki.account.model.Role;
import com.github.starwacki.repositories.SchoolClassRepository;
import com.github.starwacki.repositories.StudentRepository;
import com.github.starwacki.account.dto.AccountStudentDTO;
import com.github.starwacki.repositories.TeacherRepository;
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
