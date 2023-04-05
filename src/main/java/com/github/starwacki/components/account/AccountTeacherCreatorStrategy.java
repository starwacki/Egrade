package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.common.repositories.SchoolClassRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
class TeacherManuallyGeneratorStrategy extends AccountGeneratorStrategy {


    public TeacherManuallyGeneratorStrategy(StudentRepository studentRepository,
                                            SchoolClassRepository schoolClassRepository,
                                            TeacherRepository teacherRepository) {
        super(studentRepository, schoolClassRepository, teacherRepository);
    }

    @Override
    public Teacher createAccount(Record dto) {
        AccountTeacherDTO teacherDTO = (AccountTeacherDTO) dto;
        return Teacher.builder()
                .firstname(teacherDTO.firstname())
                .lastname(teacherDTO.lastname())
                .subject(teacherDTO.subject())
                .workPhone(teacherDTO.workPhone())
                .accountDetails(getAccountDetails(teacherDTO))
                .email(teacherDTO.email())
                .build();
    }

    private AccountDetails getAccountDetails(AccountTeacherDTO accountTeacherDTO) {
        return AccountDetails
                .builder()
                .username(generateAccountUsername(accountTeacherDTO.firstname(),accountTeacherDTO.lastname(),getLastTeacherId()))
                .password(generateFirstPassword())
                .createdDate(LocalDate.now().toString())
                .role(Role.STUDENT)
                .build();
    }

    private long getLastTeacherId() {
       return teacherRepository.count() +1;
    }

    @Override
    protected String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname +"." + lastname + "NAU"+ getLastTeacherId();
    }


}
