package com.github.starwacki.components.account;

import com.github.starwacki.common.password_encoder.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountStudentDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
class AccountStudentCreatorStrategy extends AccountCreatorStrategy {


    AccountStudentCreatorStrategy(AccountStudentRepository accountStudentRepository,
                                  AccountTeacherRepository accountTeacherRepository,
                                  EgradePasswordEncoder egradePasswordEncoder) {
        super(accountStudentRepository, accountTeacherRepository, egradePasswordEncoder);
    }

    @Override
     AccountStudent createAccount(Record dto) {
        AccountStudentDTO studentDTO = (AccountStudentDTO) dto;
        return AccountStudent.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .schoolClassName(studentDTO.className())
                .schoolClassYear(studentDTO.year())
                .accountDetails(getAccountDetails(studentDTO))
                .build();
    }

    private AccountDetails getAccountDetails(AccountStudentDTO studentDTO) {
        return AccountDetails
                .builder()
                .username(generateAccountUsername(studentDTO.firstname(),studentDTO.lastname(),getLastStudentId()))
                .password(egradePasswordEncoder.encode(generateFirstPassword()))
                .createdDate(LocalDate.now().toString())
                .accountRole(AccountRole.STUDENT)
                .build();
    }

    private long getLastStudentId() {
        return (accountStudentRepository.count() + 1);
    }

    @Override
    String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname + "." + lastname + getStudentAccountIdentity()+id;
    }

    private String getStudentAccountIdentity() {
        return "STU";
    }
}
