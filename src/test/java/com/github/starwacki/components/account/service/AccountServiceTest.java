package com.github.starwacki.components.account.service;

import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.components.account.exceptions.AccountNotFoundException;
import com.github.starwacki.components.account.exceptions.IllegalOperationException;
import com.github.starwacki.components.account.exceptions.WrongPasswordException;
import com.github.starwacki.global.model.account.Parent;
import com.github.starwacki.global.model.account.Role;
import com.github.starwacki.global.model.account.Student;
import com.github.starwacki.global.model.account.Teacher;
import com.github.starwacki.components.account.service.generator.ParentManuallyGeneratorStrategy;
import com.github.starwacki.components.account.service.generator.StudentCSVGeneratorStrategy;
import com.github.starwacki.components.account.service.generator.StudentManuallyGeneratorStrategy;
import com.github.starwacki.components.account.service.generator.TeacherManuallyGeneratorStrategy;
import com.github.starwacki.global.repositories.ParentRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import com.github.starwacki.global.model.grades.Subject;
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
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private StudentManuallyGeneratorStrategy studentManuallyGenerator;
    @Mock
    private ParentManuallyGeneratorStrategy parentManuallyGenerator;
    @Mock
    private TeacherManuallyGeneratorStrategy teacherManuallyGenerator;
    @Mock
    private StudentCSVGeneratorStrategy studentCSVGenerator;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private ParentRepository parentRepository;



    private static Student getStudentTestAccount() {
        return Student.builder()
                .firstname("firstname")
                .lastname("lastname")
                .username("username")
                .password("password")
                .role(Role.STUDENT)
                .build();
    }

    private static Teacher getTeacherTestAccount() {
        return Teacher.builder()
                .firstname("firstname")
                .lastname("lastname")
                .username("username")
                .password("password")
                .role(Role.TEACHER)
                .build();
    }

    private static Parent getParentTestAccount() {
        return Parent.builder()
                .firstname("firstname")
                .lastname("lastname")
                .username("username")
                .password("password")
                .role(Role.PARENT)
                .build();
    }

    @Test
    @DisplayName("Test change account password when giving no exist id account role should throw exception with message")
    void changeAccountPassword_givenNoExistId_shouldThrowAccountNotFoundExceptionWithNotFoundMessage() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";
        given(studentRepository.findById(id)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(AccountNotFoundException.class,() -> accountService.changeAccountPassword(role,id,oldPassword,newPassword));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage ="Account not found id: " + id;
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    @DisplayName("Test change account password when giving no exist id account role should throw exception")
    void changeAccountPassword_givenNoExistId_shouldThrowAccountNotFoundException() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";
        given(studentRepository.findById(id)).willReturn(Optional.empty());

        //then
        assertThrows(AccountNotFoundException.class,()-> accountService.changeAccountPassword(role,id,oldPassword,newPassword));
    }

    @Test
    @DisplayName("Test change account password when giving illegal role should return illegal operation exception message")
    void changeAccountPassword_givenIllegalRoleAndId_shouldThrowAccountNotFoundExceptionWithIllegalOperationMessage() {
        //given
        Role role = Role.ADMIN;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";

        //when
        Exception exception = assertThrows(IllegalOperationException.class,() -> accountService.changeAccountPassword(role,id,oldPassword,newPassword));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Illegal operation type: PUT for role: " + role;
        assertEquals(actualMessage,expectedMessage);
    }



    @Test
    @DisplayName("Test change account password when giving illegal role")
    void changeAccountPassword_givenIllegalRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        Role role = Role.ADMIN;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";

        //then
        assertThrows(IllegalOperationException.class,() -> accountService.changeAccountPassword(role,id,oldPassword,newPassword));
    }

    @Test
    @DisplayName("Test change account password return entity with changed password")
    void changeAccountPassword_givenRoleAndId_andOldPasswordSameAsNewPassword_shouldReturnAccountViewDTOWithChangedPassword() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";
        Student student = getStudentTestAccount();
        given(studentRepository.findById(id)).willReturn(Optional.of(student));
        given(studentRepository.save(student)).willReturn(student);

        //when
        AccountViewDTO accountViewDT = accountService.changeAccountPassword(role,id,oldPassword,newPassword);

        //then
        assertEquals(accountViewDT.password(),newPassword);
    }

    @Test
    @DisplayName("Test change account password given bad password throw wrong password exception")
    void changeAccountPassword_givenRoleAndId_andOldPasswordNotSameAsPasswordInDataBase_shouldThrowWrongPasswordException() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        String oldPassword = "badpassword";
        String newPassword = "changedPassword";
        Student student = getStudentTestAccount();
        given(studentRepository.findById(id)).willReturn(Optional.of(student));

        //then
        assertThrows(WrongPasswordException.class,() -> accountService.changeAccountPassword(role,id,oldPassword,newPassword));
    }

    @Test
    @DisplayName("Test change account password given bad password throw wrong password exception message")
    void changeAccountPassword_givenRoleAndId_andOldPasswordNotSameAsPasswordInDataBase_shouldThrowWrongPasswordExceptionWithMessage() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        String oldPassword = "badpassword";
        String newPassword = "changedPassword";
        Student student = getStudentTestAccount();
        given(studentRepository.findById(id)).willReturn(Optional.of(student));

        //when
        Exception exception = assertThrows(WrongPasswordException.class,() -> accountService.changeAccountPassword(role,id,oldPassword,newPassword));
        String expectedMessage = exception.getMessage();

        //then
        String actualMessage = "Password not same";
        assertEquals(expectedMessage,actualMessage);
    }



    @Test
    @DisplayName("Test change account password save entity to database")
    void changeAccountPassword_givenRoleAndId_andOldPasswordSameAsPasswordInDataBase_shouldSaveEntityWithNewPassword() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        String oldPassword = "password";
        String newPassword = "changedPassword";
        Student student = getStudentTestAccount();
        given(studentRepository.findById(id)).willReturn(Optional.of(student));
        given(studentRepository.save(student)).willReturn(student);

        //when
        accountService.changeAccountPassword(role,id,oldPassword,newPassword);

        //then
        verify(studentRepository).save(student);
    }

    @Test
    @DisplayName("Test get illegal role - should throw exception with illegal operation type message")
    void getAccountById_givenIllegalRoleAndId_shouldThrowAccountNotFoundExceptionWithMessage() {
        //given
        Role role = Role.ADMIN;
        int id = 1;

        //when
        Exception exception = assertThrows(IllegalOperationException.class, () ->  accountService.getAccountById(role,id));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Illegal operation type: GET for role: " + role;
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test get illegal role - should throw exception ")
    void getAccountById_givenIllegalRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        Role role = Role.ADMIN;
        int id = 1;

        //then
        assertThrows(IllegalOperationException.class,() -> accountService.getAccountById(role,id));
    }


    @Test
    @DisplayName("Test get account by id")
    void  getAccountById_givenTeacherRoleAndId_shouldGetTeacherFromDatabase() {
        //given
        Role role = Role.TEACHER;
        int id = 1;
        Teacher teacher = getTeacherTestAccount();
        given(teacherRepository.findById(id)).willReturn(Optional.of(teacher));

        //when
        accountService.getAccountById(role,id);

        //then
        verify(teacherRepository).findById(id);
    }

    @Test
    @DisplayName("Test get account by id return accountViewDto with correct fields")
    void  getAccountById_givenParentRoleAndId_shouldReturnAccountViewDtoWithSameFieldsLikeGivenParent() {
        //given
        Role role = Role.PARENT;
        int id = 1;
        Parent parent = getParentTestAccount();
        given(parentRepository.findById(id)).willReturn(Optional.of(parent));

        //when
        AccountViewDTO expected =  accountService.getAccountById(role,id);

        //then
        assertEquals(expected.username(),parent.getUsername());
        assertEquals(expected.firstname(),parent.getFirstname());
        assertEquals(expected.lastname(),parent.getLastname());
        assertEquals(expected.password(),parent.getPassword());
        assertEquals(expected.accountType(),parent.getRole().toString());
    }

    @Test
    @DisplayName("Test get parent account by id")
    void  getAccountById_givenParentRoleAndId_shouldGetParentFromDatabase() {
        //given
        Role role = Role.PARENT;
        int id = 1;
        Parent parent = getParentTestAccount();
        given(parentRepository.findById(id)).willReturn(Optional.of(parent));

        //when
        accountService.getAccountById(role,id);

        //then
        verify(parentRepository).findById(id);
    }

    @Test
    @DisplayName("Test get student account by id return accountViewDto with correct fields")
    void  getAccountById_givenStudentRoleAndId_shouldReturnAccountViewDtoWithSameFieldsLikeGivenStudent() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        Student student = getStudentTestAccount();
        given(studentRepository.findById(id)).willReturn(Optional.of(student));

        //when
       AccountViewDTO expected =  accountService.getAccountById(role,id);

        //then
        assertEquals(expected.username(),student.getUsername());
        assertEquals(expected.firstname(),student.getFirstname());
        assertEquals(expected.lastname(),student.getLastname());
        assertEquals(expected.password(),student.getPassword());
        assertEquals(expected.accountType(),student.getRole().toString());
    }

    @Test
    @DisplayName("Test get student account by id")
    void  getAccountById_givenStudentRoleAndId_shouldGetStudentFromDatabase() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        Student student = getStudentTestAccount();
        given(studentRepository.findById(id)).willReturn(Optional.of(student));

        //when
        accountService.getAccountById(role,id);

        //then
        verify(studentRepository).findById(id);
    }

    @Test
    @DisplayName("Test delete illegal role - should throw exception with illegal operation type message")
    void deleteAccountById_givenParentRoleAndId_shouldThrowAccountNotFoundExceptionWithMessage() {
        //given
        Role role = Role.PARENT;
        int id = 1;

        //when
        Exception exception = assertThrows(IllegalOperationException.class, () ->  accountService.deleteAccountById(role,id));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Illegal operation type: DELETE for role: " + role;
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test delete parent - should throw exception (parent is delete when student account is delete)")
    void deleteAccountById_givenParentRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        Role role = Role.PARENT;
        int id = 1;

        //then
        assertThrows(IllegalOperationException.class,() -> accountService.deleteAccountById(role,id));
    }

    @Test
    @DisplayName("Test delete no exist teacher")
    void deleteAccountById_givenNoExistTeacherRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        Role role = Role.TEACHER;
        int id = 1;
        given(teacherRepository.findById(id)).willReturn(Optional.empty());

        //then
        assertThrows(AccountNotFoundException.class,() -> accountService.deleteAccountById(role,id));
    }

    @Test
    @DisplayName("Test delete exist teacher")
    void deleteAccountById_givenExistTeacherRoleAndId_shouldDeleteTeacher() {
        //given
        Role role = Role.TEACHER;
        int id = 1;
        Teacher teacher = getTeacherTestAccount();
        given(teacherRepository.findById(id)).willReturn(Optional.of(teacher));

        //when
        accountService.deleteAccountById(role,id);

        //then
        verify(teacherRepository).delete(teacher);
    }

    @Test
    @DisplayName("Test delete no exist student")
    void deleteAccountById_givenNoExistStudentRoleAndId_shouldThrowAccountNotFoundException() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        given(studentRepository.findById(id)).willReturn(Optional.empty());

        //then
        assertThrows(AccountNotFoundException.class,() -> accountService.deleteAccountById(role,id));
    }

    @Test
    @DisplayName("Test delete exist student")
    void deleteAccountById_givenExistStudentRoleAndId_shouldDeleteStudent() {
        //given
        Role role = Role.STUDENT;
        int id = 1;
        Student student = getStudentTestAccount();
        given(studentRepository.findById(id)).willReturn(Optional.of(student));

        //when
        accountService.deleteAccountById(role,id);

        //then
        verify(studentRepository).delete(student);
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
        Teacher teacher = Teacher.builder()
                .firstname("Krzysztof")
                .lastname("Szuprych")
                .email("krzysztof@wp.pl")
                .workPhone("111222333")
                .role(Role.TEACHER)
                .subject(Subject.PHYSICS)
                .build();
        given(teacherManuallyGenerator.createAccount(accountTeacherDTO)).willReturn(teacher);
        given(teacherRepository.save(teacher)).willReturn(teacher);

        //when
        accountService.saveTeacherAccount(accountTeacherDTO);

        //then
        verify(teacherRepository).save(teacher);
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
        Teacher teacher = Teacher.builder()
                .firstname("Krzysztof")
                .lastname("Szuprych")
                .email("krzysztof@wp.pl")
                .workPhone("111222333")
                .role(Role.TEACHER)
                .subject(Subject.PHYSICS)
                .build();
        given(teacherManuallyGenerator.createAccount(accountTeacherDTO)).willReturn(teacher);
        given(teacherRepository.save(teacherManuallyGenerator.createAccount(accountTeacherDTO))).willReturn(teacher);

        //when
        AccountViewDTO expected = accountService.saveTeacherAccount(accountTeacherDTO);

        //then
        assertThat(expected.firstname(), equalTo(teacher.getFirstname()));
        assertThat(expected.lastname(), equalTo(teacher.getLastname()));
        assertThat(expected.id(), equalTo(teacher.getId()));
        assertThat(expected.username(), equalTo(teacher.getUsername()));
        assertThat(expected.password(), equalTo(teacher.getPassword()));
        assertThat(expected.accountType(), equalTo(teacher.getRole().toString()));
    }


}