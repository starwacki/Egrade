package com.github.starwacki.components.account;

import com.github.starwacki.components.auth.EgradePasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@AllArgsConstructor
abstract class AccountCreatorStrategy {

    protected final AccountStudentRepository accountStudentRepository;
    protected final AccountTeacherRepository accountTeacherRepository;
    protected final EgradePasswordEncoder egradePasswordEncoder;

    abstract Account createAccount(Record dto);

    abstract  String  generateAccountUsername(String firstname, String lastname, long id);

    String generateFirstPassword() {
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
