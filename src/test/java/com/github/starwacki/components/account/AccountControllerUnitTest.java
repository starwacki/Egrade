package com.github.starwacki.components.account;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.components.account.exceptions.AccountNotFoundException;
import com.github.starwacki.components.account.exceptions.IllegalOperationException;
import com.github.starwacki.components.account.exceptions.WrongFileException;
import com.github.starwacki.components.account.exceptions.WrongPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AccountController.class,
        excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = OncePerRequestFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AccountControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountFacade accountFacade;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Test add student return 202 HTTP status and created AccountViewDTO in responseBody")
    void addStudent_givenAccountStudentDTO_shouldReturn_202_HTTPStatus_andResponseBodyWithAccountViewDTO() throws Exception {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .className("2A")
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();
        AccountViewDTO accountViewDTO = AccountViewDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .accountType(AccountRole.STUDENT.toString())
                .id(1)
                .password("password")
                .username("Firstname.Lastname1STU")
                .build();
        given(accountFacade.saveStudentAndParentAccount(accountStudentDTO)).willReturn(accountViewDTO);

        //when
        ResultActions response= mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(accountViewDTO);
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "AD",
                    "firstnameWithSpecialCharacter%%%",
                    "firstnameIsLongerThanFortyCharactersqqqqqqqqqqqqq",
                    "firstnameWithNumber11"
            })
    @DisplayName("Test validation AccountStudentDTO incorrect firstname return 400 HTTP status and error message in responseBody")
    void addStudent_givenIncorrectFirstname_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage(String firstname) throws Exception {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .firstname(firstname)
                .lastname("Lastname")
                .className("2A")
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"firstname\":\"Invalid value: must match \\\"^[A-Za-z]{3,40}$\\\"\"}";
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "AD",
                    "lastnameWithSpecialCharacter%%%",
                    "lastnameIsLongerThanFortyCharactersqqqqqqqqqqqqq",
                    "lastnameWithNumber11"
            })
    @DisplayName("Test validation AccountStudentDTO incorrect lastname return 400 HTTP status and error message in responseBody")
    void addStudent_givenIncorrectLastname_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage(String lastname) throws Exception {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .firstname("firstname")
                .lastname(lastname)
                .className("2A")
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"lastname\":\"Invalid value: must match \\\"^[A-Za-z]{3,40}$\\\"\"}";
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -5,0,2019,2041,11111111
    })
    @DisplayName("Test validation AccountStudentDTO incorrect year return 400 HTTP status and error message in responseBody")
    void addStudent_givenIncorrectYear_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage(int year) throws Exception {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .className("2A")
                .year(year)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "{\"year\":\"Invalid value: must be between 2020 and 2040\"}";
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "11", "A1", "1a","1AA","1AAA","KLASA","1aA","1*",
            })
    @DisplayName("Test validation AccountStudentDTO incorrect class name return 400 HTTP status ")
    void addStudent_givenIncorrectClassName_shouldReturn_400_HTTPStatus(String className) throws Exception {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .className(className)
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "12345678910","lettersInNumber1","12345678","-12345678","-123456789","","       "
            })
    @DisplayName("Test validation AccountStudentDTO incorrect parent phone number return 400 HTTP status")
    void addStudent_givenIncorrectParentPhoneNumber_shouldReturn_400_HTTPStatus(String phone) throws Exception {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .className("2A")
                .year(2023)
                .parentPhoneNumber(phone)
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Test validation AccountStudentDTO without fields return 400 HTTP status")
    void addStudent_givenAccountStudentDtoWithoutFields_shouldReturn_400_HTTPStatus() throws Exception {
        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Test add student from csv throw given wrong file return 400 HTTP status and error message in response body")
    void addStudentsFromCSVFile_givenWrongFilePath_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage() throws Exception {
        //given
        String wrongFileFilePath = "wrong";
        given(accountFacade.saveStudentsAndParentsFromFile(wrongFileFilePath)).willThrow(new WrongFileException(WrongFileException.Code.FILE));


        //when
        ResultActions response= mockMvc.perform(post("/account/students")
                        .param("pathname",wrongFileFilePath)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "File is empty or file can't be read";
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @Test
    @DisplayName("Test add student from csv throw given file with wrong year in row return 400 HTTP status and error message in response body")
    void addStudentsFromCSVFile_givenWrongYearFormatInFile_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage() throws Exception {
        //given
        String filePath = "correct";
        int wrongLine = 2;
        given(accountFacade.saveStudentsAndParentsFromFile(filePath)).willThrow(new WrongFileException(WrongFileException.Code.YEAR_FORMAT,wrongLine));


        //when
        ResultActions response= mockMvc.perform(post("/account/students")
                .param("pathname",filePath)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "Wrong year format, line number: " + wrongLine;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @Test
    @DisplayName("Test add student from csv throw given file with wrong row return 400 HTTP status and error message in response body")
    void addStudentsFromCSVFile_givenWrongFileDataRow_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage() throws Exception {
        //given
        String filePath = "correct";
        int wrongLine = 2;
        given(accountFacade.saveStudentsAndParentsFromFile(filePath)).willThrow(new WrongFileException(WrongFileException.Code.LINE,wrongLine));


        //when
        ResultActions response= mockMvc.perform(post("/account/students")
                .param("pathname",filePath)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "Line is empty or not have call, line number: " + wrongLine;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @Test
    @DisplayName("Test add student from csv throw given file with wrong dto field return 400 HTTP status and error message in response body")
    void addStudentsFromCSVFile_givenIncorrectFieldToMapToAccountStudentDTO_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage() throws Exception {
        //given
        String filePath = "correct";
        int wrongLine = 2;
        given(accountFacade.saveStudentsAndParentsFromFile(filePath)).willThrow(new WrongFileException(WrongFileException.Code.VALIDATION,wrongLine));


        //when
        ResultActions response= mockMvc.perform(post("/account/students")
                .param("pathname",filePath)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "Incorrect data format, line number: " + wrongLine;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @Test
    @DisplayName("Test add student from csv return 201 HTTP status and List of created AccountStudentDTO in response body")
    void addStudentsFromCSVFile_givenCorrectFile_shouldReturn_200_HTTPStatus_andResponseBodyWithListOfAccountViewDTOS() throws Exception {
        //given
        String filePath = "correct";
        AccountViewDTO accountStudentDTO = AccountViewDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .username("firstname.lastname1STU")
                .accountType(AccountRole.STUDENT.toString())
                .password("password")
                .build();
        List<AccountViewDTO> listOfCreatedAccounts = List.of( accountStudentDTO,accountStudentDTO,accountStudentDTO);
        given(accountFacade.saveStudentsAndParentsFromFile(filePath)).willReturn(listOfCreatedAccounts);


        //when
        ResultActions response= mockMvc.perform(post("/account/students")
                .param("pathname",filePath)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(listOfCreatedAccounts);
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @Test
    @DisplayName("Test add teacher return 201 HTTP status and created AccountViewDto in responseBody")
    void addTeacher_givenAccountTeacherDTO_shouldReturn_200_HTTPStatus_andResponseBodyWithCreatedDTO() throws Exception {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .workPhone("111222333")
                .email("email@wp.pl")
                .subject("PHYSICS")
                .build();
        AccountViewDTO accountViewDTO = AccountViewDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .accountType(AccountRole.TEACHER.toString())
                .id(1)
                .password("password")
                .username("firstname.lastname1NAU")
                .build();
        given(accountFacade.saveTeacherAccount(accountTeacherDTO)).willReturn(accountViewDTO);

        //when
        ResultActions response= mockMvc.perform(post("/account/teacher")
                .content(objectMapper.writeValueAsString(accountTeacherDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = objectMapper.writeValueAsString(accountViewDTO);
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "AD",
                    "firstnameWithSpecialCharacter%%%",
                    "firstnameIsLongerThanFortyCharactersqqqqqqqqqqqqq",
                    "firstnameWithNumber11"
            })
    @DisplayName("Test validation AccountTeacherDTO incorrect firstname return 400 HTTP status and error message in responseBody")
    void addTeacher_givenWrongFirstname_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage(String firstname) throws Exception {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO
                .builder()
                .firstname(firstname)
                .lastname("lastname")
                .workPhone("111222333")
                .email("email@wp.pl")
                .subject("PHYSICS")
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/teacher")
                .content(objectMapper.writeValueAsString(accountTeacherDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"firstname\":\"Invalid value: must match \\\"^[A-Za-z]{3,40}$\\\"\"}";
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "AD",
                    "lastnameWithSpecialCharacter%%%",
                    "lastnameIsLongerThanFortyCharactersqqqqqqqqqqqqq",
                    "lastnameWithNumber11"
            })
    @DisplayName("Test validation AccountTeacherDTO incorrect lastname return 400 HTTP status and error message in responseBody")
    void addTeacher_givenIncorrectLastname_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage(String lastname) throws Exception {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO
                .builder()
                .firstname("firstname")
                .lastname(lastname)
                .workPhone("111222333")
                .email("email@wp.pl")
                .subject("PHYSICS")
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/teacher")
                .content(objectMapper.writeValueAsString(accountTeacherDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"lastname\":\"Invalid value: must match \\\"^[A-Za-z]{3,40}$\\\"\"}";
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "12345678910","lettersInNumber1","12345678","-12345678","-123456789","","       "
            })
    @DisplayName("Test validation AccountStudentDTO incorrect parent phone number return 400 HTTP status")
    void addTeacher_givenIncorrectWorkPhoneNumber_shouldReturn_400_HTTPStatus(String phone) throws Exception {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .workPhone(phone)
                .email("email@wp.pl")
                .subject("PHYSICS")
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/teacher")
                .content(objectMapper.writeValueAsString(accountTeacherDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "username.@domain.com",".user.name@domain.com","user-name@domain.com.","username@.com","","       "
            })
    @DisplayName("Test validation AccountTeacherDTO incorrect email return 400 HTTP status")
    void addTeacher_givenIncorrectEmail_shouldReturn_400_HTTPStatus(String email) throws Exception {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .workPhone("111222333")
                .email(email)
                .subject("PHYSICS")
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/teacher")
                .content(objectMapper.writeValueAsString(accountTeacherDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Test validation AccountTeacherDTO without fields return 400 HTTP status")
    void addTeacher_givenAccountTeacherDTOWithoutFields_shouldReturn_400_HTTPStatus() throws Exception {
        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO
                .builder()
                .build();

        //when
        ResultActions response= mockMvc.perform(post("/account/teacher")
                .content(objectMapper.writeValueAsString(accountTeacherDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @ParameterizedTest
    @EnumSource(value = AccountRole.class,names = {"STUDENT","TEACHER","PARENT"})
    @DisplayName("Test get account by id given legal role return 200 HTTP status and AccountViewDTO in response body")
    void  getAccountById_givenCorrectRole_shouldReturn_200_HTTPStatus_andResponseBodyWithAccountViewDto(AccountRole accountRole) throws Exception {
        //given
        int accountId = 1;
        AccountViewDTO accountViewDTO = AccountViewDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .id(accountId)
                .accountType(accountRole.toString())
                .password("password")
                .build();
        given(accountFacade.getAccountById(accountRole,accountId)).willReturn(accountViewDTO);

        //when
        ResultActions response= mockMvc.perform(get("/account/"+ accountRole +"="+accountId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(accountViewDTO);
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }
    @Test
    @DisplayName("Test get account by id given ADMIN role return 400 HTTP status and error message in response body")
    void  getAccountById_givenAdminRole_shouldReturn_400_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
        //given
        int accountId = 1;
        AccountRole accountRole = AccountRole.ADMIN;
        given(accountFacade.getAccountById(accountRole,accountId)).willThrow(new IllegalOperationException(HttpMethod.GET, AccountRole.ADMIN.toString()));

        //when
        ResultActions response= mockMvc.perform(get("/account/"+ accountRole +"="+accountId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "Illegal operation type: " + HttpMethod.GET + " for role: " + accountRole;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @EnumSource(value = AccountRole.class,names = {"STUDENT","TEACHER","PARENT"})
    @DisplayName("Test get account by id when account no exist return 404 HTTP status and error message in response body")
    void  getAccountById_givenCorrectRoleAndNoExistAccountId_shouldReturn_404_HTTPStatus_andResponseBodyErrorMessage(AccountRole accountRole) throws Exception {
        //given
        int accountId = 1;
        given(accountFacade.getAccountById(accountRole,accountId)).willThrow(new AccountNotFoundException(accountId));

        //when
        ResultActions response= mockMvc.perform(get("/account/"+ accountRole +"="+accountId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody =  "Account not found id: " + accountId;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @EnumSource(value = AccountRole.class,names = {"STUDENT","TEACHER"})
    @DisplayName("Test delete account by id given legal role return 200 HTTP status and AccountViewDTO in response body")
    void  deleteAccountById_givenCorrectRole_shouldReturn_200_HTTPStatus_andResponseBodyWithAccountViewDto(AccountRole accountRole) throws Exception {
        //given
        int accountId = 1;
        AccountViewDTO accountViewDTO = AccountViewDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .id(accountId)
                .accountType(accountRole.toString())
                .password("password")
                .build();
        given(accountFacade.deleteAccountById(accountRole,accountId)).willReturn(accountViewDTO);

        //when
        ResultActions response= mockMvc.perform(delete("/account/"+ accountRole +"="+accountId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(accountViewDTO);
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @EnumSource(value = AccountRole.class,names = {"ADMIN","PARENT"})
    @DisplayName("Test delete account by id given illegal role return 400 HTTP status and error message in response body")
    void  deleteAccountById_givenIllegalRole_shouldReturn_400_HTTPStatus_andResponseBodyWithErrorMessage(AccountRole accountRole) throws Exception {
        //given
        int accountId = 1;
        given(accountFacade.deleteAccountById(accountRole,accountId)).willThrow(new IllegalOperationException(HttpMethod.DELETE, accountRole.toString()));

        //when
        ResultActions response= mockMvc.perform(delete("/account/"+ accountRole +"="+accountId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "Illegal operation type: " + HttpMethod.DELETE + " for role: " + accountRole;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @EnumSource(value = AccountRole.class,names = {"STUDENT","TEACHER"})
    @DisplayName("Test get account by id when account no exist return 404 HTTP status and error message in response body")
    void  deleteAccountById_givenCorrectRoleAndNoExistAccountId_shouldReturn_404_HTTPStatus_andResponseBodyErrorMessage(AccountRole accountRole) throws Exception {
        //given
        int accountId = 1;
        given(accountFacade.deleteAccountById(accountRole,accountId)).willThrow(new AccountNotFoundException(accountId));

        //when
        ResultActions response= mockMvc.perform(delete("/account/"+ accountRole +"="+accountId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody =  "Account not found id: " + accountId;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @ValueSource(strings =  {
            "short",
            "LongerThan25CharactersPassword.43141412",
            "withoutSpecialChar",
            "withoutNumber.",
            "wihoutbigletters1."
    })
    @DisplayName("Test change account password return 200 HTTP status and AccountViewDTO in response body")
    void  changeAccountPassword_givenIncorrectPasswordPattern_shouldReturn_400_HTTPStatus(String newPassword) throws Exception {
        //given
        int accountId = 1;
        String oldPassword = "oldpassword";
        AccountRole accountRole = AccountRole.STUDENT;


        //when
        ResultActions response= mockMvc.perform(put("/account/password/"+ accountRole +"="+accountId)
                .param("oldPassword",oldPassword)
                .param("newPassword",newPassword)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @ParameterizedTest
    @EnumSource(value = AccountRole.class,names = {"STUDENT","TEACHER","PARENT"})
    @DisplayName("Test change account password return 200 HTTP status and AccountViewDTO in response body")
    void  changeAccountPassword_givenCorrectRole_shouldReturn_200_HTTPStatus_andResponseBodyWithAccountViewDto(AccountRole accountRole) throws Exception {
        //given
        int accountId = 1;
        String oldPassword = "oldpassword";
        String newPassword = "Password1.";
        AccountViewDTO accountViewDTO = AccountViewDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .id(accountId)
                .accountType(accountRole.toString())
                .password(newPassword)
                .build();
        given(accountFacade.changeAccountPassword(accountRole,accountId,oldPassword,newPassword)).willReturn(accountViewDTO);

        //when
        ResultActions response= mockMvc.perform(put("/account/password/"+ accountRole +"="+accountId)
                .param("oldPassword",oldPassword)
                .param("newPassword",newPassword)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(accountViewDTO);
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @EnumSource(value = AccountRole.class,names = {"ADMIN","PARENT"})
    @DisplayName("Test change account by id given illegal role return 400 HTTP status and error message in response body")
    void  changeAccountPassword_givenIllegalRole_shouldReturn_400_HTTPStatus_andResponseBodyWithErrorMessage(AccountRole accountRole) throws Exception {
        //given
        int accountId = 1;
        String oldPassword = "oldpassword";
        String newPassword = "Password1.";
        given(accountFacade.changeAccountPassword(accountRole,accountId,oldPassword,newPassword)).willThrow(new IllegalOperationException(HttpMethod.PUT, accountRole.toString()));

        //when
        ResultActions response= mockMvc.perform(put("/account/password/"+ accountRole +"="+accountId)
                .param("oldPassword",oldPassword)
                .param("newPassword",newPassword)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "Illegal operation type: " + HttpMethod.PUT + " for role: " + accountRole;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @EnumSource(value = AccountRole.class,names = {"STUDENT","TEACHER","PARENT"})
    @DisplayName("Test get account by id when account no exist return 404 HTTP status and error message in response body")
    void  changeAccountPassword_givenCorrectRoleAndNoExistAccountId_shouldReturn_404_HTTPStatus_andResponseBodyErrorMessage(AccountRole accountRole) throws Exception {
        //given
        int accountId = 1;
        String oldPassword = "oldpassword";
        String newPassword = "Password1.";
        given(accountFacade.changeAccountPassword(accountRole,accountId,oldPassword,newPassword)).willThrow(new AccountNotFoundException(accountId));

        //when
        ResultActions response= mockMvc.perform(put("/account/password/"+ accountRole +"="+accountId)
                .param("oldPassword",oldPassword)
                .param("newPassword",newPassword)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody =  "Account not found id: " + accountId;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @EnumSource(value = AccountRole.class,names = {"STUDENT","TEACHER","PARENT"})
    @DisplayName("Test get account by id when password not same return 400 HTTP status and error message in response body")
    void  changeAccountPassword_givenPasswordNotSameLikePasswordInDatabase_shouldReturn_400_HTTPStatus_andResponseBodyErrorMessage(AccountRole accountRole) throws Exception {
        //given
        int accountId = 1;
        String oldPassword = "oldpassword";
        String newPassword = "Password1.";
        given(accountFacade.changeAccountPassword(accountRole,accountId,oldPassword,newPassword)).willThrow(new WrongPasswordException());

        //when
        ResultActions response= mockMvc.perform(put("/account/password/"+ accountRole +"="+accountId)
                .param("oldPassword",oldPassword)
                .param("newPassword",newPassword)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody =  "Password not same";
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

}
