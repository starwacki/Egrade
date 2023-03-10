package com.github.starwacki.components.account.service.generator;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.global.model.account.Student;
import com.github.starwacki.global.repositories.SchoolClassRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.model.school_class.SchoolClass;
import com.github.starwacki.global.security.AES;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StudentManuallyGeneratorUnitTest {

    @InjectMocks
    private StudentManuallyGeneratorStrategy studentManuallyGenerator;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SchoolClassRepository schoolClassRepository;

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
        Student expected = studentManuallyGenerator.createAccount(accountStudentDTO);

        //then
        assertThat(expected,
                allOf(
                        hasProperty("firstname",equalTo(accountStudentDTO.firstname())),
                        hasProperty("lastname",equalTo(accountStudentDTO.lastname()))
                )
        );
    }

    @Test
    @DisplayName("Test generating student will have given school class fields")
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentWithGivenInDTOSchoolClassWhenSchoolClassExist() {
        //given
        int year = 2022;
        String className = "2A";
        AccountStudentDTO accountStudentDTO = AccountStudentDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(year)
                .className(className)
                .parentPhoneNumber("111222333")
                .build();
        given(schoolClassRepository.findSchoolClassByNameAndAndClassYear(className,year))
                .willReturn(Optional.of(new SchoolClass(className,year)));

        //when
        Student generatedStudent = studentManuallyGenerator.createAccount(accountStudentDTO);
        SchoolClass expected = schoolClassRepository.findSchoolClassByNameAndAndClassYear(className,year).get();

        //then
        assertThat(expected,
                allOf(
                        hasProperty("classYear",equalTo(generatedStudent.getSchoolClass().getClassYear())),
                        hasProperty("name",equalTo(generatedStudent.getSchoolClass().getName()))
                )
        );
    }

    @Test
    @DisplayName("Test generating student will have given school class fields when class isn't in database")
    void generateStudentAccount_givenAccountStudentDTO_shouldReturnStudentWithGivenInDTOSchoolClassWhenSchoolClassNoExist() {
        //given
        int year = 2022;
        String className = "2A";
        AccountStudentDTO accountStudentDTO = AccountStudentDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .year(year)
                .className(className)
                .parentPhoneNumber("111222333")
                .build();
        given(schoolClassRepository.findSchoolClassByNameAndAndClassYear(className,year))
                .willReturn(Optional.empty());

        //when
        Student generatedStudent = studentManuallyGenerator.createAccount(accountStudentDTO);
        SchoolClass expected = new SchoolClass(className,year);

        //then
        assertThat(expected,
                allOf(
                        hasProperty("classYear",equalTo(generatedStudent.getSchoolClass().getClassYear())),
                        hasProperty("name",equalTo(generatedStudent.getSchoolClass().getName()))
                )
        );
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
        given(studentRepository.count()).willReturn(0l);

        //when
        Student expected = studentManuallyGenerator.createAccount(accountStudentDTO);
        long thisStudentId = studentRepository.count()+1;
        String studentUsernamePattern = accountStudentDTO.firstname()+ "."+ accountStudentDTO.lastname() + "STU"+thisStudentId;

        //then
        assertThat(expected,hasProperty("username",equalTo(studentUsernamePattern)));
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
        Student expected = studentManuallyGenerator.createAccount(accountStudentDTO);
        int passwordLength = 10;

        //then
        String decryptedPassword = AES.decrypt(expected.getPassword());
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
        Student expected = studentManuallyGenerator.createAccount(accountStudentDTO);

        //then
        String decryptedPassword = AES.decrypt(expected.getPassword());
        assertThat(decryptedPassword, matchesPattern("^[A-Za-z]+$"));
    }


}