package com.github.starwacki.components.account;

import com.github.starwacki.components.auth.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountStudentRequestDTO;
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
        AccountStudentRequestDTO studentDTO = (AccountStudentRequestDTO) dto;
        return AccountStudent.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .schoolClassName(studentDTO.className())
                .schoolClassYear(studentDTO.year())
                .accountDetails(getAccountDetails(studentDTO))
                .build();
    }

    private AccountDetails getAccountDetails(AccountStudentRequestDTO studentDTO) {
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
