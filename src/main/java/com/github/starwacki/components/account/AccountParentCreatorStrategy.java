package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.common.repositories.SchoolClassRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
                .accountDetails(getAccountDetails(studentDTO))
                .phoneNumber(studentDTO.parentPhoneNumber())
                .build();
    }

    private AccountDetails getAccountDetails(AccountStudentDTO studentDTO) {
        return AccountDetails
                .builder()
                .username(generateAccountUsername(studentDTO.firstname(),studentDTO.lastname(),getLastStudentId()))
                .password(generateFirstPassword())
                .createdDate(LocalDate.now().toString())
                .role(Role.STUDENT)
                .build();
    }

    @Override
    protected String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname + "." + lastname + "RO"+id;
    }

}
