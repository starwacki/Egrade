package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
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

    public AccountStudent createStudent(AccountStudentDTO accountStudentDTO) {
        return accountStudentCreatorStrategy.createAccount(accountStudentDTO);
    }

    public AccountParent createParent(AccountStudentDTO accountStudentDTO) {
        return accountParentCreatorStrategy.createAccount(accountStudentDTO);
    }

    public AccountTeacher createTeacher(AccountTeacherDTO accountTeacherDTO) {
        return  accountTeacherCreatorStrategy.createAccount(accountTeacherDTO);
    }

    public List<AccountStudentDTO> createStudents(String path) {
        return accountStudentCsvCreatorStrategy.generateStudents(path);
    }
}
