package com.github.starwacki.components.account;

import com.github.starwacki.common.password_encoder.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountStudentDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
class AccountParentCreatorStrategy extends AccountCreatorStrategy {


    AccountParentCreatorStrategy(AccountStudentRepository accountStudentRepository,
                                 AccountTeacherRepository accountTeacherRepository,
                                 EgradePasswordEncoder egradePasswordEncoder) {
        super(accountStudentRepository, accountTeacherRepository,egradePasswordEncoder);
    }

    private long getLastStudentId() {
        return (accountStudentRepository.count() + 1);
    }

    @Override
    public AccountParent createAccount(Record dto) {
        AccountStudentDTO studentDTO = (AccountStudentDTO) dto;
        return AccountParent.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .accountDetails(getAccountDetails(studentDTO))
                .phoneNumber(studentDTO.parentPhoneNumber())
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

    @Override
    protected String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname + "." + lastname + "RO"+id;
    }

}
