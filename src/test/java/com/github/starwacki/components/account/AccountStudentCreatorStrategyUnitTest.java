package com.github.starwacki.components.account;

import com.github.starwacki.components.auth.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountStudentRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AccountStudentCreatorStrategyUnitTest {

    private static AccountStudentCreatorStrategy accountStudentCreatorStrategy;
    private static AccountStudentRepository accountStudentRepository;
    private static EgradePasswordEncoder egradePasswordEncoder;

    @BeforeAll
    static void setup() {
        accountStudentRepository = mock(AccountStudentRepository.class);
        AccountTeacherRepository accountTeacherRepository = mock(AccountTeacherRepository.class);
        egradePasswordEncoder = new EgradePasswordEncoderAccountSTUB();
        accountStudentCreatorStrategy = new AccountStudentCreatorStrategy(accountStudentRepository,accountTeacherRepository,egradePasswordEncoder);
    }


    @Test
    @DisplayName("Test generating student with same fields like given DTO")
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentAccount() {
        //given
        AccountStudentRequestDTO accountStudentRequestDTO = AccountStudentRequestDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();

        //when
        AccountStudent expected = accountStudentCreatorStrategy.createAccount(accountStudentRequestDTO);

        //then
        assertThat(expected.getFirstname(),is(equalTo(accountStudentRequestDTO.firstname())));
        assertThat(expected.getLastname(),is(equalTo(accountStudentRequestDTO.lastname())));
        assertThat(expected.getSchoolClassYear(),is(equalTo(accountStudentRequestDTO.year())));
        assertThat(expected.getSchoolClassName(),is(equalTo(accountStudentRequestDTO.className())));
    }

    @Test
    @DisplayName("Test generating student username pattern")
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentWithStudentUsernamePattern() {
        //given
        AccountStudentRequestDTO accountStudentRequestDTO = AccountStudentRequestDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();
        given(accountStudentRepository.count()).willReturn(0l);

        //when
        AccountStudent expected = accountStudentCreatorStrategy.createAccount(accountStudentRequestDTO);
        long thisStudentId = accountStudentRepository.count()+1;
        String studentUsernamePattern = accountStudentRequestDTO.firstname()+ "."+ accountStudentRequestDTO.lastname() + "STU"+thisStudentId;

        //then
        assertThat(expected.getAccountDetails().getUsername(),is(equalTo(studentUsernamePattern)));
    }

    @Test
    @DisplayName("Test generating student first password length is 10")
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentWithTenLetterPassword() {
        //given
        AccountStudentRequestDTO accountStudentRequestDTO = AccountStudentRequestDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();

        //when
        AccountStudent expected = accountStudentCreatorStrategy.createAccount(accountStudentRequestDTO);
        int passwordLength = 10;

        //then
        String decryptedPassword = expected.getAccountDetails().getPassword();
        assertThat(decryptedPassword.length(), is(passwordLength));
    }

    @DisplayName("Test generating student first random password does not have any special characters")
    @RepeatedTest(10)
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentWithNoneSpecialCharactersPassword() {
        //given
        AccountStudentRequestDTO accountStudentRequestDTO = AccountStudentRequestDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();


        //when
        AccountStudent expected = accountStudentCreatorStrategy.createAccount(accountStudentRequestDTO);

        //then
        String decryptedPassword = expected.getAccountDetails().getPassword();
        assertThat(decryptedPassword, matchesPattern("^[A-Za-z]+$"));
    }


}