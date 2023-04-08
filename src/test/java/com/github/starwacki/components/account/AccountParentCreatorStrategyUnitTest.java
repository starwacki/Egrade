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
class AccountParentCreatorStrategyUnitTest {

    private static AccountParentCreatorStrategy accountParentCreatorStrategy;
    private static AccountTeacherRepository accountTeacherRepository;
    private static AccountStudentRepository accountStudentRepository;
    private static EgradePasswordEncoder egradePasswordEncoder;


    @BeforeAll
    public static void  setUp() {
        accountTeacherRepository = mock(AccountTeacherRepository.class);
        accountStudentRepository = mock(AccountStudentRepository.class);
        egradePasswordEncoder = new EgradePasswordEncoderAccountSTUB();
        accountParentCreatorStrategy = new AccountParentCreatorStrategy(accountStudentRepository,accountTeacherRepository,egradePasswordEncoder);
    }

    @Test
    @DisplayName("Test generating parent with same fields like given student DTO")
    void generateParentAccount_givenAccountStudentDTO_shouldReturnParentAccount() {
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
        AccountParent expected = accountParentCreatorStrategy.createAccount(accountStudentRequestDTO);

        //then
        assertThat(expected.getFirstname(),is(equalTo(accountStudentRequestDTO.firstname())));
        assertThat(expected.getLastname(),is(equalTo(accountStudentRequestDTO.lastname())));
        assertThat(expected.getPhoneNumber(),is(equalTo(accountStudentRequestDTO.parentPhoneNumber())));
    }

    @Test
    @DisplayName("Test generating parent username pattern")
    void generateParentAccount_givenAccountStudentDTO_shouldReturnParentWithParentUsernamePattern() {
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
        AccountParent expected = accountParentCreatorStrategy.createAccount(accountStudentRequestDTO);
        long thisParentId = accountStudentRepository.count()+1;
        String parentUsernamePattern = accountStudentRequestDTO.firstname()+ "."+ accountStudentRequestDTO.lastname() + "RO"+thisParentId;

        //then
        assertThat(expected.getAccountDetails().getUsername(),is(equalTo(parentUsernamePattern)));
    }

    @Test
    @DisplayName("Test generating parent first password length is 10")
    void generateParentAccount_givenAccountStudentDTO_shouldReturnParentWithTenLetterPassword() {
        //given
        AccountStudentRequestDTO accountStudentRequestDTO = AccountStudentRequestDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();

        //when
        AccountParent expected = accountParentCreatorStrategy.createAccount(accountStudentRequestDTO);
        int passwordLength = 10;

        //then
        String decryptedPassword = expected.getAccountDetails().getPassword();
        assertThat(decryptedPassword.length(), is(passwordLength));
    }

    @DisplayName("Test generating parent first random password does not have any special characters")
    @RepeatedTest(10)
    void generateParentAccount_givenAccountParentDTO_shouldReturnParentWithNoneSpecialCharactersPassword() {
        //given
        AccountStudentRequestDTO accountStudentRequestDTO = AccountStudentRequestDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("111222333")
                .build();

        //when
        AccountParent expected = accountParentCreatorStrategy.createAccount(accountStudentRequestDTO);

        //then
        String decryptedPassword = expected.getAccountDetails().getPassword();
        assertThat(decryptedPassword, matchesPattern("^[A-Za-z]+$"));
    }


}