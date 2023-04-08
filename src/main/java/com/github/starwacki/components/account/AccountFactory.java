package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountStudentRequestDTO;
import com.github.starwacki.components.account.dto.AccountTeacherRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class AccountFactory {

    private final AccountStudentCreatorStrategy accountStudentCreatorStrategy;
    private final AccountTeacherCreatorStrategy accountTeacherCreatorStrategy;
    private final AccountParentCreatorStrategy accountParentCreatorStrategy;
    private final AccountStudentCsvCreatorStrategy accountStudentCsvCreatorStrategy;

    public AccountStudent createStudent(AccountStudentRequestDTO accountStudentRequestDTO) {
        return accountStudentCreatorStrategy.createAccount(accountStudentRequestDTO);
    }

    public AccountParent createParent(AccountStudentRequestDTO accountStudentRequestDTO) {
        return accountParentCreatorStrategy.createAccount(accountStudentRequestDTO);
    }

    public AccountTeacher createTeacher(AccountTeacherRequestDTO accountTeacherRequestDTO) {
        return  accountTeacherCreatorStrategy.createAccount(accountTeacherRequestDTO);
    }

    public List<AccountStudentRequestDTO> createStudents(String path) {
        return accountStudentCsvCreatorStrategy.generateStudents(path);
    }
}
