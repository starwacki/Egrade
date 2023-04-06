package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccountStudentCreatorStrategyUnitTest {

    @InjectMocks
    private AccountStudentCreatorStrategy accountStudentCreatorStrategy;
    @Mock
    private AccountStudentRepository accountStudentRepository;


    @Test
    @DisplayName("Test generating student with same fields like given DTO")
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentAccount() {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();

        //when
        AccountStudent expected = accountStudentCreatorStrategy.createAccount(accountStudentDTO);

        //then
        assertThat(expected.getFirstname(),is(equalTo(accountStudentDTO.firstname())));
        assertThat(expected.getLastname(),is(equalTo(accountStudentDTO.lastname())));
        assertThat(expected.getSchoolClassYear(),is(equalTo(accountStudentDTO.year())));
        assertThat(expected.getSchoolClassName(),is(equalTo(accountStudentDTO.className())));
    }

    @Test
    @DisplayName("Test generating student username pattern")
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentWithStudentUsernamePattern() {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();
        given(accountStudentRepository.count()).willReturn(0l);

        //when
        AccountStudent expected = accountStudentCreatorStrategy.createAccount(accountStudentDTO);
        long thisStudentId = accountStudentRepository.count()+1;
        String studentUsernamePattern = accountStudentDTO.firstname()+ "."+ accountStudentDTO.lastname() + "STU"+thisStudentId;

        //then
        assertThat(expected.getAccountDetails().getUsername(),is(equalTo(studentUsernamePattern)));
    }

    @Test
    @DisplayName("Test generating student first password length is 10")
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentWithTenLetterPassword() {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();

        //when
        AccountStudent expected = accountStudentCreatorStrategy.createAccount(accountStudentDTO);
        int passwordLength = 10;

        //then
        String decryptedPassword = expected.getAccountDetails().getPassword();
        assertThat(decryptedPassword.length(), is(passwordLength));
    }

    @DisplayName("Test generating student first random password does not have any special characters")
    @RepeatedTest(10)
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentWithNoneSpecialCharactersPassword() {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();


        //when
        AccountStudent expected = accountStudentCreatorStrategy.createAccount(accountStudentDTO);

        //then
        String decryptedPassword = expected.getAccountDetails().getPassword();
        assertThat(decryptedPassword, matchesPattern("^[A-Za-z]+$"));
    }


}