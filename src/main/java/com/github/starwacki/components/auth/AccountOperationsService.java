package com.github.starwacki.components.auth;


import com.github.starwacki.common.model.account.Account;
import com.github.starwacki.common.repositories.AdminRepository;
import com.github.starwacki.common.repositories.ParentRepository;
import com.github.starwacki.common.repositories.StudentRepository;
import com.github.starwacki.common.repositories.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
class AccountOperationsService {

    private static final String STUDENT_ACCOUNT_VERIFIER = "STU";
    private static final String TEACHER_ACCOUNT_VERIFIER = "NAU";
    private static final String PARENT_ACCOUNT_VERIFIER = "RO";
    private static final String ADMIN_ACCOUNT_VERIFIER = "ADM";
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final AdminRepository adminRepository;

    public Optional<Account> findAccountByUsername(String username) {
        if (username.contains(STUDENT_ACCOUNT_VERIFIER))
            return studentRepository.findByUsername(username);
        else if (username.contains(TEACHER_ACCOUNT_VERIFIER))
            return teacherRepository.findByUsername(username);
        else if (username.contains(PARENT_ACCOUNT_VERIFIER))
            return parentRepository.findByUsername(username);
        else if (username.contains(ADMIN_ACCOUNT_VERIFIER)) {
            return adminRepository.findByUsername(username);
        } else
            return Optional.empty();
    }

}
