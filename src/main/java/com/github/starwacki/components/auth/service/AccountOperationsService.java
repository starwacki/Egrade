package com.github.starwacki.components.auth.service;


import com.github.starwacki.global.model.account.Account;
import com.github.starwacki.global.repositories.ParentRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
class AccountOperationsService {

    private static final String STUDENT_ACCOUNT_VERIFIER = "STU";
    private static final String TEACHER_ACCOUNT_VERIFIER = "NAU";
    private static final String PARENT_ACCOUNT_VERIFIER = "RO";
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;

    public Optional<Account> findAccountByUsername(String username) {
        if (username.contains(STUDENT_ACCOUNT_VERIFIER))
            return studentRepository.findByUsername(username);
        else if (username.contains(TEACHER_ACCOUNT_VERIFIER))
            return teacherRepository.findByUsername(username);
        else if (username.contains(PARENT_ACCOUNT_VERIFIER))
            return parentRepository.findByUsername(username);
        else
            return Optional.empty();
    }

}
