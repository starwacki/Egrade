package com.github.starwacki.components.account.service.generator;

import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.model.Teacher;
import com.github.starwacki.global.repositories.TeacherRepository;
import com.github.starwacki.components.student.model.Subject;
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
class TeacherManuallyGeneratorTest {

    @InjectMocks
    private TeacherManuallyGeneratorStrategy teacherManuallyGenerator;
    @Mock
    private TeacherRepository teacherRepository;

    @Test
    @DisplayName("Test generating teacher with same fields like given DTO")
    void generateTeacherAccount_givenAccountTeacherDTO_shouldReturnTeacherAccount() {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO.builder()
                .firstname("teacher")
                .lastname("lastname")
                .workPhone("111222333")
                .subject(Subject.BIOLOGY)
                .email("email@wp.pl")
                .build();

        //when
        Teacher expected = teacherManuallyGenerator.createAccount(accountTeacherDTO);

        //then
        assertThat(expected,
                allOf(
                hasProperty("firstname",equalTo(accountTeacherDTO.firstname())),
                hasProperty("lastname",equalTo(accountTeacherDTO.lastname())),
                hasProperty("workPhone",equalTo(accountTeacherDTO.workPhone())),
                hasProperty("subject",equalTo(accountTeacherDTO.subject())),
                hasProperty("email",equalTo(accountTeacherDTO.email()))
                )
        );
    }

    @Test
    @DisplayName("Test generating teacher username pattern")
    void generateTeacherAccount_givenAccountTeacherDTO_shouldReturnTeacherWithTeacherUsernamePattern() {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO.builder()
                .firstname("teacher")
                .lastname("lastname")
                .build();
        given(teacherRepository.count()).willReturn(0l);

        //when
        Teacher expected = teacherManuallyGenerator.createAccount(accountTeacherDTO);
        long thisTeacherId = teacherRepository.count()+1;
        String teacherUsernamePattern = accountTeacherDTO.firstname()+ "."+ accountTeacherDTO.lastname() + "NAU"+thisTeacherId;

        //then
        assertThat(expected,hasProperty("username",equalTo(teacherUsernamePattern)));
    }

    @Test
    @DisplayName("Test generating teacher first password length is 10")
    void generateTeacherAccount_givenAccountTeacherDTO_shouldReturnTeacherWithTenLetterPassword() {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO.builder()
                .build();

        //when
        Teacher expected = teacherManuallyGenerator.createAccount(accountTeacherDTO);
        int passwordLength = 10;

        //then
        assertThat(expected.getPassword().length(), is(passwordLength));
    }

    @DisplayName("Test generating teacher first random password does not have any special characters")
    @RepeatedTest(10)
    void generateTeacherAccount_givenAccountTeacherDTO_shouldReturnTeacherWithNoneSpecialCharactersPassword() {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO.builder()
                .build();

        //when
        Teacher expected = teacherManuallyGenerator.createAccount(accountTeacherDTO);

        //then
        assertThat(expected.getPassword(), matchesPattern("^[A-Za-z]+$"));
    }



}