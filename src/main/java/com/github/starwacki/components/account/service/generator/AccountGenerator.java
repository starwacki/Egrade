package com.github.starwacki.components.account.service.generator;

import com.github.starwacki.repositories.SchoolClassRepository;
import com.github.starwacki.repositories.StudentRepository;
import com.github.starwacki.repositories.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@AllArgsConstructor
public abstract class AccountGenerator {

    protected final StudentRepository studentRepository;

    protected final SchoolClassRepository schoolClassRepository;

    protected final TeacherRepository teacherRepository;

    protected abstract  String  generateAccountUsername(String firstname, String lastname, long id);

    protected String generateFirstPassword() {
        StringBuilder stringBuilder = new StringBuilder();
        addRandomNumber(stringBuilder);
        return stringBuilder.toString();
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
