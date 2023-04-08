package com.github.starwacki.components.account;

import com.github.starwacki.components.auth.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountTeacherRequestDTO;
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
class AccountTeacherCreatorStrategyUnitTest {

    private static AccountTeacherCreatorStrategy accountTeacherCreatorStrategy;
    private static AccountTeacherRepository accountTeacherRepository;
    private static EgradePasswordEncoder egradePasswordEncoder;

    @BeforeAll
    static void setup() {
        AccountStudentRepository accountStudentRepository = mock(AccountStudentRepository.class);
        accountTeacherRepository = mock(AccountTeacherRepository.class);
        egradePasswordEncoder = new EgradePasswordEncoderAccountSTUB();
        accountTeacherCreatorStrategy = new AccountTeacherCreatorStrategy(accountStudentRepository,accountTeacherRepository,egradePasswordEncoder);
    }




    @Test
    @DisplayName("Test generating teacher with same fields like given DTO")
    void generateTeacherAccount_givenAccountTeacherDTO_shouldReturnTeacherAccount() {
        //given
        AccountTeacherRequestDTO accountTeacherRequestDTO = AccountTeacherRequestDTO.builder()
                .firstname("teacher")
                .lastname("lastname")
                .workPhone("111222333")
                .subject("BIOLOGY")
                .email("email@wp.pl")
                .build();

        //when
        AccountTeacher expected = accountTeacherCreatorStrategy.createAccount(accountTeacherRequestDTO);

        //then
        assertThat(expected.getFirstname(),is(equalTo(accountTeacherRequestDTO.firstname())));
        assertThat(expected.getLastname(),is(equalTo(accountTeacherRequestDTO.lastname())));
        assertThat(expected.getWorkPhone(),is(equalTo(accountTeacherRequestDTO.workPhone())));
        assertThat(expected.getSubject(),is(equalTo(accountTeacherRequestDTO.subject())));
        assertThat(expected.getEmail(),is(equalTo(accountTeacherRequestDTO.email())));
    }

    @Test
    @DisplayName("Test generating teacher username pattern")
    void generateTeacherAccount_givenAccountTeacherDTO_shouldReturnTeacherWithTeacherUsernamePattern() {
        //given
        AccountTeacherRequestDTO accountTeacherRequestDTO = AccountTeacherRequestDTO.builder()
                .firstname("teacher")
                .lastname("lastname")
                .build();
        given(accountTeacherRepository.count()).willReturn(0l);

        //when
        AccountTeacher expected = accountTeacherCreatorStrategy.createAccount(accountTeacherRequestDTO);
        long thisTeacherId = accountTeacherRepository.count()+1;
        String teacherUsernamePattern = accountTeacherRequestDTO.firstname()+ "."+ accountTeacherRequestDTO.lastname() + "NAU"+thisTeacherId;

        //then
        assertThat(expected.getAccountDetails().getUsername(),is(equalTo(teacherUsernamePattern)));
    }

    @Test
    @DisplayName("Test generating teacher first password length is 10")
    void generateTeacherAccount_givenAccountTeacherDTO_shouldReturnTeacherWithTenLetterPassword() {
        //given
        AccountTeacherRequestDTO accountTeacherRequestDTO = AccountTeacherRequestDTO.builder()
                .build();

        //when
        AccountTeacher expected = accountTeacherCreatorStrategy.createAccount(accountTeacherRequestDTO);
        int passwordLength = 10;

        //then
        String decryptedPassword = egradePasswordEncoder.decode(expected.getAccountDetails().getPassword());
        assertThat(decryptedPassword.length(), is(passwordLength));
    }

    @DisplayName("Test generating teacher first random password does not have any special characters")
    @RepeatedTest(10)
    void generateTeacherAccount_givenAccountTeacherDTO_shouldReturnTeacherWithNoneSpecialCharactersPassword() {
        //given
        AccountTeacherRequestDTO accountTeacherRequestDTO = AccountTeacherRequestDTO.builder()
                .build();

        //when
        AccountTeacher expected = accountTeacherCreatorStrategy.createAccount(accountTeacherRequestDTO);

        //then
        String decryptedPassword = egradePasswordEncoder.decode(expected.getAccountDetails().getPassword());
        assertThat(decryptedPassword, matchesPattern("^[A-Za-z]+$"));
    }



}