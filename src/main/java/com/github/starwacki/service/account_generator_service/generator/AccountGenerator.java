package com.github.starwacki.service.account_generator_service.generator;

import com.github.starwacki.model.account.Account;
import com.github.starwacki.model.account.Role;
import com.github.starwacki.model.account.Student;
import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import com.github.starwacki.service.account_generator_service.dto.AccountStudentDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@AllArgsConstructor
public abstract class AccountGenerator {

    protected final StudentRepository studentRepository;

    protected final SchoolClassRepository schoolClassRepository;

       /*
          Method generate two account: generate account for student and account for student's parent,
          parent account is assigned to specified student.
          first password  is randomly generated.
         */

    protected String generateFirstPassword() {
        StringBuilder stringBuilder = new StringBuilder();
        addRandomNumber(stringBuilder);
        return stringBuilder.toString();
    }

    protected long getLastStudentId() {
        return (studentRepository.count() + 1);
    }

    private void addRandomNumber(StringBuilder stringBuilder) {
        Random random = new Random();
        int randomNumber;
        for (int i = 0; i < 10; i++) {
            randomNumber = random.nextInt(65, 123);
            if (isCharLetter(randomNumber)) {
                randomNumber += 10;
            }
            stringBuilder.append((char) (randomNumber));
        }
    }

    private boolean isCharLetter(int randomNumber) {
        return randomNumber >= 91 && randomNumber <= 96;
    }





}
