package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.components.account.exceptions.AccountNotFoundException;
import com.github.starwacki.components.account.exceptions.IllegalOperationException;
import com.github.starwacki.components.account.exceptions.WrongPasswordException;
import com.github.starwacki.common.model.grades.Subject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountFacadeUnitTest {

    @InjectMocks
    private AccountFacade accountFacade;
    @Mock
    private AccountFactory accountFactory;
    @Mock
    private AccountStudentRepository accountStudentRepository;
    @Mock
    private AccountTeacherRepository accountTeacherRepository;
    @Mock
    private AccountParentRepository accountParentRepository;



    private static AccountStudent getStudentTestAccount() {
        return  AccountStudent
                .builder()
                .accountDetails(AccountDetails
                        .builder()
                        .username("username")
                        .password("password")
                        .accountRole(AccountRole.STUDENT)
                        .build())
                .firstname("firstname")
                .lastname("lastname")
                .schoolClassName("1A")
                .schoolClassYear(2023)
                .build();
    }

    private static AccountTeacher getTeacherTestAccount() {
        return AccountTeacher.builder()
                .firstname("firstname")
                .lastname("lastname")
                .accountDetails(AccountDetails
                                .builder()
                                .username("username")
                                .password("password")
                                .accountRole(AccountRole.TEACHER)
                                .build())
                .build();
    }

    private static AccountParent getParentTestAccount() {
        return AccountParent.builder()
                .firstname("firstname")
                .lastname("lastname")
                .accountDetails(AccountDetails
                        .builder()
                        .username("username")
                        .password("password")
                        .accountRole(AccountRole.TEACHER)
                        .build())
                .build();
    }

    @Test
    @DisplayName("Test change account password when giving no exist id account role should throw exception with message")
    void changeAccountPassword_givenNoExistId_shouldThrowAccountNotFoundExceptionWithNotFoundMessage() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";
        given(accountStudentRepository.findById(id)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(AccountNotFoundException.class,() -> accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage ="Account not found id: " + id;
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    @DisplayName("Test change account password when giving no exist id account role should throw exception")
    void changeAccountPassword_givenNoExistId_shouldThrowAccountNotFoundException() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";
        given(accountStudentRepository.findById(id)).willReturn(Optional.empty());

        //then
        assertThrows(AccountNotFoundException.class,()-> accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword));
    }

    @Test
    @DisplayName("Test change account password when giving illegal role should return illegal operation exception message")
    void changeAccountPassword_givenIllegalRoleAndId_shouldThrowAccountNotFoundExceptionWithIllegalOperationMessage() {
        //given
        AccountRole accountRole = AccountRole.ADMIN;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";

        //when
        Exception exception = assertThrows(IllegalOperationException.class,() -> accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Illegal operation type: PUT for role: " + accountRole;
        assertEquals(actualMessage,expectedMessage);
    }



    @Test
    @DisplayName("Test change account password when giving illegal role")
    void changeAccountPassword_givenIllegalRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        AccountRole accountRole = AccountRole.ADMIN;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";

        //then
        assertThrows(IllegalOperationException.class,() -> accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword));
    }

    @Test
    @DisplayName("Test change account password return entity with changed password")
    void changeAccountPassword_givenRoleAndId_andOldPasswordSameAsNewPassword_shouldReturnAccountViewDTOWithChangedPassword() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";
        AccountStudent accountStudent = getStudentTestAccount();
        given(accountStudentRepository.findById(id)).willReturn(Optional.of(accountStudent));
        given(accountStudentRepository.save(accountStudent)).willReturn(accountStudent);

        //when
        System.out.println(accountStudent.getAccountDetails().getPassword());
        AccountViewDTO accountViewDT = accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword);

        //then
        assertEquals(accountViewDT.password(),newPassword);
    }

    @Test
    @DisplayName("Test change account password given bad password throw wrong password exception")
    void changeAccountPassword_givenRoleAndId_andOldPasswordNotSameAsPasswordInDataBase_shouldThrowWrongPasswordException() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        String oldPassword = "badpassword";
        String newPassword = "changedPassword";
        AccountStudent accountStudent = getStudentTestAccount();
        given(accountStudentRepository.findById(id)).willReturn(Optional.of(accountStudent));

        //then
        assertThrows(WrongPasswordException.class,() -> accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword));
    }

    @Test
    @DisplayName("Test change account password given bad password throw wrong password exception message")
    void changeAccountPassword_givenRoleAndId_andOldPasswordNotSameAsPasswordInDataBase_shouldThrowWrongPasswordExceptionWithMessage() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        String oldPassword = "badpassword";
        String newPassword = "changedPassword";
        AccountStudent accountStudent = getStudentTestAccount();
        given(accountStudentRepository.findById(id)).willReturn(Optional.of(accountStudent));

        //when
        Exception exception = assertThrows(WrongPasswordException.class,() -> accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword));
        String expectedMessage = exception.getMessage();

        //then
        String actualMessage = "Password not same";
        assertEquals(expectedMessage,actualMessage);
    }



    @Test
    @DisplayName("Test change account password save entity to database")
    void changeAccountPassword_givenRoleAndId_andOldPasswordSameAsPasswordInDataBase_shouldSaveEntityWithNewPassword() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";
        AccountStudent accountStudent = getStudentTestAccount();
        given(accountStudentRepository.findById(id)).willReturn(Optional.of(accountStudent));
        given(accountStudentRepository.save(accountStudent)).willReturn(accountStudent);

        //when
        accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword);

        //then
        verify(accountStudentRepository).save(accountStudent);
    }

    @Test
    @DisplayName("Test get illegal role - should throw exception with illegal operation type message")
    void getAccountById_givenIllegalRoleAndId_shouldThrowAccountNotFoundExceptionWithMessage() {
        //given
        AccountRole accountRole = AccountRole.ADMIN;
        int id = 1;

        //when
        Exception exception = assertThrows(IllegalOperationException.class, () ->  accountFacade.getAccountById(accountRole,id));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Illegal operation type: GET for role: " + accountRole;
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test get illegal role - should throw exception ")
    void getAccountById_givenIllegalRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        AccountRole accountRole = AccountRole.ADMIN;
        int id = 1;

        //then
        assertThrows(IllegalOperationException.class,() -> accountFacade.getAccountById(accountRole,id));
    }


    @Test
    @DisplayName("Test get account by id")
    void  getAccountById_givenTeacherRoleAndId_shouldGetTeacherFromDatabase() {
        //given
        AccountRole accountRole = AccountRole.TEACHER;
        int id = 1;
        AccountTeacher accountTeacher = getTeacherTestAccount();
        given(accountTeacherRepository.findById(id)).willReturn(Optional.of(accountTeacher));

        //when
        accountFacade.getAccountById(accountRole,id);

        //then
        verify(accountTeacherRepository).findById(id);
    }

    @Test
    @DisplayName("Test get account by id return accountViewDto with correct fields")
    void  getAccountById_givenParentRoleAndId_shouldReturnAccountViewDtoWithSameFieldsLikeGivenParent() {
        //given
        AccountRole accountRole = AccountRole.PARENT;
        int id = 1;
        AccountParent accountParent = getParentTestAccount();
        given(accountParentRepository.findById(id)).willReturn(Optional.of(accountParent));

        //when
        AccountViewDTO expected =  accountFacade.getAccountById(accountRole,id);

        //then
        assertEquals(expected.username(), accountParent.getAccountDetails().getUsername());
        assertEquals(expected.firstname(), accountParent.getFirstname());
        assertEquals(expected.lastname(), accountParent.getLastname());
        assertEquals(expected.password(),accountParent.getAccountDetails().getPassword());
        assertEquals(expected.accountType(), accountParent.getAccountDetails().getAccountRole().toString());
    }

    @Test
    @DisplayName("Test get parent account by id")
    void  getAccountById_givenParentRoleAndId_shouldGetParentFromDatabase() {
        //given
        AccountRole accountRole = AccountRole.PARENT;
        int id = 1;
        AccountParent accountParent = getParentTestAccount();
        given(accountParentRepository.findById(id)).willReturn(Optional.of(accountParent));
        System.out.println(accountParent.getAccountDetails().getPassword());

        //when
        accountFacade.getAccountById(accountRole,id);

        //then
        verify(accountParentRepository).findById(id);
    }

    @Test
    @DisplayName("Test get student account by id return accountViewDto with correct fields")
    void  getAccountById_givenStudentRoleAndId_shouldReturnAccountViewDtoWithSameFieldsLikeGivenStudent() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        AccountStudent accountStudent = getStudentTestAccount();
        given(accountStudentRepository.findById(id)).willReturn(Optional.of(accountStudent));

        //when
       AccountViewDTO expected =  accountFacade.getAccountById(accountRole,id);

        //then
        assertEquals(expected.username(), accountStudent.getAccountDetails().getUsername());
        assertEquals(expected.firstname(), accountStudent.getFirstname());
        assertEquals(expected.lastname(), accountStudent.getLastname());
        assertEquals(expected.password(),accountStudent.getAccountDetails().getPassword());
        assertEquals(expected.accountType(), accountStudent.getAccountDetails().getAccountRole().toString());
    }

    @Test
    @DisplayName("Test get student account by id")
    void  getAccountById_givenStudentRoleAndId_shouldGetStudentFromDatabase() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        AccountStudent accountStudent = getStudentTestAccount();
        given(accountStudentRepository.findById(id)).willReturn(Optional.of(accountStudent));

        //when
        accountFacade.getAccountById(accountRole,id);

        //then
        verify(accountStudentRepository).findById(id);
    }

    @Test
    @DisplayName("Test delete illegal role - should throw exception with illegal operation type message")
    void deleteAccountById_givenParentRoleAndId_shouldThrowAccountNotFoundExceptionWithMessage() {
        //given
        AccountRole accountRole = AccountRole.PARENT;
        int id = 1;

        //when
        Exception exception = assertThrows(IllegalOperationException.class, () ->  accountFacade.deleteAccountById(accountRole,id));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Illegal operation type: DELETE for role: " + accountRole;
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test delete parent - should throw exception (parent is delete when student account is delete)")
    void deleteAccountById_givenParentRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        AccountRole accountRole = AccountRole.PARENT;
        int id = 1;

        //then
        assertThrows(IllegalOperationException.class,() -> accountFacade.deleteAccountById(accountRole,id));
    }

    @Test
    @DisplayName("Test delete no exist teacher")
    void deleteAccountById_givenNoExistTeacherRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        AccountRole accountRole = AccountRole.TEACHER;
        int id = 1;
        given(accountTeacherRepository.findById(id)).willReturn(Optional.empty());

        //then
        assertThrows(AccountNotFoundException.class,() -> accountFacade.deleteAccountById(accountRole,id));
    }

    @Test
    @DisplayName("Test delete exist teacher")
    void deleteAccountById_givenExistTeacherRoleAndId_shouldDeleteTeacher() {
        //given
        AccountRole accountRole = AccountRole.TEACHER;
        int id = 1;
        AccountTeacher accountTeacher = getTeacherTestAccount();
        given(accountTeacherRepository.findById(id)).willReturn(Optional.of(accountTeacher));

        //when
        accountFacade.deleteAccountById(accountRole,id);

        //then
        verify(accountTeacherRepository).delete(accountTeacher);
    }

    @Test
    @DisplayName("Test delete no exist student")
    void deleteAccountById_givenNoExistStudentRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        given(accountStudentRepository.findById(id)).willReturn(Optional.empty());

        //then
        assertThrows(AccountNotFoundException.class,() -> accountFacade.deleteAccountById(accountRole,id));
    }

    @Test
    @DisplayName("Test delete exist student")
    void deleteAccountById_givenExistStudentRoleAndId_shouldDeleteStudent() {
        //given
        AccountRole accountRole = AccountRole.STUDENT;
        int id = 1;
        AccountStudent accountStudent = getStudentTestAccount();
        given(accountStudentRepository.findById(id)).willReturn(Optional.of(accountStudent));

        //when
        accountFacade.deleteAccountById(accountRole,id);

        //then
        verify(accountStudentRepository).delete(accountStudent);
    }

    @Test
    @DisplayName("Test saving teacher")
    void saveTeacherAccount_givenTeacherAccountDTO_shouldSaveTeacher() {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO.builder()
                .firstname("Krzysztof")
                .lastname("Szuprych")
                .email("krzysztof@wp.pl")
                .workPhone("111222333")
                .subject(Subject.PHYSICS)
                .build();
        AccountTeacher accountTeacher = AccountTeacher.builder()
                .firstname("Krzysztof")
                .lastname("Szuprych")
                .email("krzysztof@wp.pl")
                .accountDetails(AccountDetails
                        .builder()
                        .password("password")
                        .accountRole(AccountRole.TEACHER)
                        .build())
                .workPhone("111222333")
                .subject(Subject.PHYSICS)
                .build();
        given(accountFactory.createTeacher(accountTeacherDTO)).willReturn(accountTeacher);
        given(accountTeacherRepository.save(accountTeacher)).willReturn(accountTeacher);

        //when
        accountFacade.saveTeacherAccount(accountTeacherDTO);

        //then
        verify(accountTeacherRepository).save(accountTeacher);
    }

    @Test
    @DisplayName("Test saving teacher account dto return accountViewDto with same fields")
    void saveTeacherAccount_givenTeacherAccountDTO_shouldReturnAccountViewDTOWithSameFieldsLikeGivingDTO() {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO.builder()
                .firstname("Krzysztof")
                .lastname("Szuprych")
                .email("krzysztof@wp.pl")
                .workPhone("111222333")
                .subject(Subject.PHYSICS)
                .build();
        AccountTeacher accountTeacher = AccountTeacher.builder()
                .firstname("Krzysztof")
                .lastname("Szuprych")
                .email("krzysztof@wp.pl")
                .accountDetails(AccountDetails
                        .builder()
                        .password("password")
                        .accountRole(AccountRole.TEACHER)
                        .build())
                .workPhone("111222333")
                .subject(Subject.PHYSICS)
                .build();
        given(accountFactory.createTeacher(accountTeacherDTO)).willReturn(accountTeacher);
        given(accountTeacherRepository.save(accountFactory.createTeacher(accountTeacherDTO))).willReturn(accountTeacher);

        //when
        AccountViewDTO expected = accountFacade.saveTeacherAccount(accountTeacherDTO);

        //then
        assertThat(expected.firstname(), equalTo(accountTeacher.getFirstname()));
        assertThat(expected.lastname(), equalTo(accountTeacher.getLastname()));
        assertThat(expected.id(), equalTo(accountTeacher.getId()));
        assertThat(expected.username(), equalTo(accountTeacher.getAccountDetails().getUsername()));
        assertThat(expected.password(), equalTo(accountTeacher.getAccountDetails().getPassword()));
        assertThat(expected.accountType(), equalTo(accountTeacher.getAccountDetails().getAccountRole().toString()));
    }


}