package com.github.starwacki.components.account.factory;

import com.github.starwacki.common.model.account.Parent;
import com.github.starwacki.common.model.account.Role;
import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.common.repositories.SchoolClassRepository;
import com.github.starwacki.common.repositories.StudentRepository;
import com.github.starwacki.common.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
class ParentManuallyGeneratorStrategy extends AccountGeneratorStrategy {


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
