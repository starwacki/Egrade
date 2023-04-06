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
class AccountParentCreatorStrategyUnitTest {

    @InjectMocks
    private AccountParentCreatorStrategy accountParentCreatorStrategy;
    @Mock
    private AccountParentRepository accountParentRepository;

    @Mock
    private AccountStudentRepository accountStudentRepository;


    @Test
    @DisplayName("Test generating parent with same fields like given student DTO")
    void generateParentAccount_givenAccountStudentDTO_shouldReturnParentAccount() {
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
        AccountParent expected = accountParentCreatorStrategy.createAccount(accountStudentDTO);

        //then
        assertThat(expected.getFirstname(),is(equalTo(accountStudentDTO.firstname())));
        assertThat(expected.getLastname(),is(equalTo(accountStudentDTO.lastname())));
        assertThat(expected.getPhoneNumber(),is(equalTo(accountStudentDTO.parentPhoneNumber())));
    }

    @Test
    @DisplayName("Test generating parent username pattern")
    void generateParentAccount_givenAccountStudentDTO_shouldReturnParentWithParentUsernamePattern() {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();
        given(accountParentRepository.count()).willReturn(0l);

        //when
        AccountParent expected = accountParentCreatorStrategy.createAccount(accountStudentDTO);
        long thisParentId = accountParentRepository.count()+1;
        String parentUsernamePattern = accountStudentDTO.firstname()+ "."+ accountStudentDTO.lastname() + "RO"+thisParentId;

        //then
        assertThat(expected.getAccountDetails().getUsername(),is(equalTo(parentUsernamePattern)));
    }

    @Test
    @DisplayName("Test generating parent first password length is 10")
    void generateParentAccount_givenAccountStudentDTO_shouldReturnParentWithTenLetterPassword() {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();

        //when
        AccountParent expected = accountParentCreatorStrategy.createAccount(accountStudentDTO);
        int passwordLength = 10;

        //then
        String decryptedPassword = expected.getAccountDetails().getPassword();
        assertThat(decryptedPassword.length(), is(passwordLength));
    }

    @DisplayName("Test generating parent first random password does not have any special characters")
    @RepeatedTest(10)
    void generateParentAccount_givenAccountParentDTO_shouldReturnParentWithNoneSpecialCharactersPassword() {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();

        //when
        AccountParent expected = accountParentCreatorStrategy.createAccount(accountStudentDTO);

        //then
        String decryptedPassword = expected.getAccountDetails().getPassword();
        assertThat(decryptedPassword, matchesPattern("^[A-Za-z]+$"));
    }


}