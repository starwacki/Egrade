package com.github.starwacki.components.account;

import com.github.starwacki.common.password_encoder.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
class AccountTeacherCreatorStrategy extends AccountCreatorStrategy {


    public AccountTeacherCreatorStrategy(AccountStudentRepository accountStudentRepository,
                                         AccountTeacherRepository accountTeacherRepository,
                                         EgradePasswordEncoder egradePasswordEncoder) {
        super(accountStudentRepository, accountTeacherRepository, egradePasswordEncoder);
    }

    @Override
    public AccountTeacher createAccount(Record dto) {
        AccountTeacherDTO teacherDTO = (AccountTeacherDTO) dto;
        return AccountTeacher.builder()
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
                .password(egradePasswordEncoder.encode(generateFirstPassword()))
                .createdDate(LocalDate.now().toString())
                .accountRole(AccountRole.STUDENT)
                .build();
    }

    private long getLastTeacherId() {
       return accountTeacherRepository.count() +1;
    }

    @Override
    protected String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname +"." + lastname + "NAU"+ getLastTeacherId();
    }


}
