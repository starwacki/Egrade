package com.github.starwacki.components.auth;


import com.github.starwacki.common.model.account.Account;
import com.github.starwacki.common.model.account.Parent;
import com.github.starwacki.common.model.account.Student;
import com.github.starwacki.common.model.account.Teacher;
import com.github.starwacki.common.repositories.ParentRepository;
import com.github.starwacki.common.repositories.StudentRepository;
import com.github.starwacki.common.repositories.TeacherRepository;
import com.github.starwacki.components.auth.AccountOperationsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountOperationServiceUnitTest {

    @InjectMocks
    private AccountOperationsService accountOperationsService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private TeacherRepository teacherRepository;

    @Test
    @DisplayName("Test find account by username given username with student verifier search username in studentRepository")
    void  findAccountByUsername_givenUsernameWithStudentVerifier_shouldSearchAccountInStudentRepository() {

        //given
        String username = "StudentTestSTU1";
        Student student = Student
                .builder()
                .firstname(username)
                .build();
        given(studentRepository.findByUsername(username)).willReturn(Optional.of(student));

        //when
        accountOperationsService.findAccountByUsername(username);

        //then
        verify(studentRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Test find account by username given username with teacher verifier search username in teacherRepository")
    void  findAccountByUsername_givenUsernameWithTeacherVerifier_shouldSearchAccountInTeacherRepository() {

        //given
        String username = "TeacherTestNAU1";
        Teacher teacher = Teacher
                .builder()
                .firstname(username)
                .build();
        given(teacherRepository.findByUsername(username)).willReturn(Optional.of(teacher));

        //when
        accountOperationsService.findAccountByUsername(username);

        //then
        verify(teacherRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Test find account by username given username with parent verifier search username in parentRepository")
    void  findAccountByUsername_givenUsernameWithParentVerifier_shouldSearchAccountInParentRepository() {

        //given
        String username = "ParentTestRO1";
        Parent parent = Parent
                .builder()
                .firstname(username)
                .build();
        given(parentRepository.findByUsername(username)).willReturn(Optional.of(parent));

        //when
        accountOperationsService.findAccountByUsername(username);

        //then
        verify(parentRepository).findByUsername(username);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Student","Parent","usernameWithoutVerifier"})
    @DisplayName("Test find account by username given wrong username verifier return empty Optional")
    void  findAccountByUsername_givenUsernameWithWrongVerifier_shouldReturnOptionalEmpty(String username) {

        //when
        Optional<Account> optionalAccount = accountOperationsService.findAccountByUsername(username);

        //then
        assertThat(optionalAccount,is(Optional.empty()));
    }



}
