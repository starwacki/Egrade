package com.github.starwacki.components.account.service.generator;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.global.model.account.Parent;
import com.github.starwacki.global.repositories.ParentRepository;
import com.github.starwacki.global.repositories.StudentRepository;
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
class ParentManuallyGeneratorUnitTest {

    @InjectMocks
    private ParentManuallyGeneratorStrategy parentManuallyGenerator;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private StudentRepository studentRepository;

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


        //when
        Parent expected = parentManuallyGenerator.createAccount(accountStudentDTO);

        //then
        assertThat(expected,
                allOf(
                        hasProperty("firstname",equalTo( accountStudentDTO.firstname())),
                        hasProperty("lastname",equalTo( accountStudentDTO.lastname())),
                        hasProperty("phoneNumber",equalTo( accountStudentDTO.parentPhoneNumber())))
        );
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
        given(parentRepository.count()).willReturn(0l);

        //when
        Parent expected = parentManuallyGenerator.createAccount(accountStudentDTO);
        long thisParentId = parentRepository.count()+1;
        String parentUsernamePattern = accountStudentDTO.firstname()+ "."+ accountStudentDTO.lastname() + "RO"+thisParentId;

        //then
        assertThat(expected,hasProperty("username",equalTo(parentUsernamePattern)));
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
        Parent expected = parentManuallyGenerator.createAccount(accountStudentDTO);
        int passwordLength = 10;

        //then
        assertThat(expected.getPassword().length(), is(passwordLength));
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
        Parent expected = parentManuallyGenerator.createAccount(accountStudentDTO);

        //then
        assertThat(expected.getPassword(), matchesPattern("^[A-Za-z]+$"));
    }


}