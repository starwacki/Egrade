package com.github.starwacki.components.account.factory;

import com.github.starwacki.common.model.account.Parent;
import com.github.starwacki.common.model.account.Student;
import com.github.starwacki.common.model.account.Teacher;
import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountFactory {

    private final StudentManuallyGeneratorStrategy studentManuallyGeneratorStrategy;
    private final TeacherManuallyGeneratorStrategy teacherManuallyGeneratorStrategy;
    private final ParentManuallyGeneratorStrategy parentManuallyGeneratorStrategy;
    private final StudentCSVGeneratorStrategy studentCSVGeneratorStrategy;

    public Student createStudent(AccountStudentDTO accountStudentDTO) {
        return studentManuallyGeneratorStrategy.createAccount(accountStudentDTO);
    }

    public Parent createParent(AccountStudentDTO accountStudentDTO) {
        return parentManuallyGeneratorStrategy.createAccount(accountStudentDTO);
    }

    public Teacher createTeacher(AccountTeacherDTO accountTeacherDTO) {
        return  teacherManuallyGeneratorStrategy.createAccount(accountTeacherDTO);
    }

    public List<AccountStudentDTO> createStudents(String path) {
        return studentCSVGeneratorStrategy.generateStudents(path);
    }
}
