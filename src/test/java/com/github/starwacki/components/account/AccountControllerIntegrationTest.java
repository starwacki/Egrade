package com.github.starwacki.components.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.auth.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountStudentRequestDTO;
import com.github.starwacki.components.account.dto.AccountTeacherRequestDTO;
import com.github.starwacki.components.account.dto.AccountResponseDTO;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountControllerIntegrationTest {

    private static final String AUTH_COOKIE_NAME = "egrade-jwt";
    private static final String STUDENT_JWT_TOKEN = " eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTVFVERU5UIiwiaWF0IjoxNjgxNDA2OTMyLCJleHAiOjI1NDU0MDY5MzJ9.tSP-VwINn-uWEjoX_nCrLLzJeGPzexCnSZu2Ss3RC5k";
    private static final String TEACHER_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJURUFDSEVSIiwiaWF0IjoxNjgxNDA2OTMyLCJleHAiOjI1NDU0MDY5MzJ9.W6-Do2H5XNq8lBU_8y47hIUA4-BcypemWBS5HYb90Hg";
    private static final String PARENT_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQQVJFTlQiLCJpYXQiOjE2ODE0MDY5MzIsImV4cCI6MjU0NTQwNjkzMn0.ZHDI0HKH2anwf9VTwawu6EygcqvQI90Y4qQpXGb3CwE";
    private static final String ADMIN_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBRE1JTiIsImlhdCI6MTY4MTQwNjkzMiwiZXhwIjoyNTQ1NDA2OTMyfQ.IgsgZz42Ba8SxlIav5eBFbdqGnym8GswFPwOHp6QrsQ";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountStudentRepository accountStudentRepository;
    @Autowired
    private AccountParentRepository accountParentRepository;
    @Autowired
    private AccountTeacherRepository accountTeacherRepository;
    @Autowired
    private EgradePasswordEncoder egradePasswordEncoder;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void prepareDatabase() {
        String studentAuthDetails = "INSERT INTO DETAILS VALUES ('STUDENT','STUDENT', '-', 'v+Tt7O3O3S8S2zQuS7lF0w==')";
        String teacherAuthDetails = "INSERT INTO DETAILS VALUES ('TEACHER','TEACHER', '-', 'v+Tt7O3O3S8S2zQuS7lF0w==')";
        String parentAuthDetails =  "INSERT INTO DETAILS VALUES ('PARENT','PARENT', '-', 'v+Tt7O3O3S8S2zQuS7lF0w==')";
        String adminAuthDetails =   "INSERT INTO DETAILS VALUES ('ADMIN','ADMIN', '-', 'v+Tt7O3O3S8S2zQuS7lF0w==')";
        jdbcTemplate.execute(studentAuthDetails);
        jdbcTemplate.execute(teacherAuthDetails);
        jdbcTemplate.execute(parentAuthDetails);
        jdbcTemplate.execute(adminAuthDetails);
    }


    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,TEACHER_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test add student with roles without permissions return 403 HTTP status")
    void addStudent_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        AccountStudentRequestDTO accountStudentRequestDTO = AccountStudentRequestDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .className("2A")
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/account/student")
                .cookie(egradeJWTCookie)
                .content(objectMapper.writeValueAsString(accountStudentRequestDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test add student with ADMIN role return 201 HTTP status and should save student to database")
    void addStudent_givenAdminRole_shouldReturn_201_HTTPStatus_andCreateStudentAccountWithProperFields() throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,ADMIN_JWT_TOKEN);
        AccountStudentRequestDTO accountStudentRequestDTO = AccountStudentRequestDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .className("2A")
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/account/student")
                .cookie(egradeJWTCookie)
                .content(objectMapper.writeValueAsString(accountStudentRequestDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        List<AccountStudent> studentsInDataBase = accountStudentRepository.findAll();
        AccountStudent student = studentsInDataBase.get(0);
        assertThat(studentsInDataBase, hasSize(1));
        assertThat(student.getFirstname(),is(equalTo(accountStudentRequestDTO.firstname())));
        assertThat(student.getLastname(),is(equalTo(accountStudentRequestDTO.lastname())));
        assertThat(student.getAccountDetails().getUsername(),is(equalTo("Firstname.LastnameSTU1")));
    }

    @Test
    @DisplayName("Test add student with ADMIN role return 201 HTTP status and should save parent to database")
    void addStudent_givenAdminRole_shouldReturn_201_HTTPStatus_andCreateParentAccountWithProperFields() throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,ADMIN_JWT_TOKEN);
        AccountStudentRequestDTO accountStudentRequestDTO = AccountStudentRequestDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .className("2A")
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/account/student")
                .cookie(egradeJWTCookie)
                .content(objectMapper.writeValueAsString(accountStudentRequestDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));

        List<AccountParent> parentsInDatabase = accountParentRepository.findAll();
        AccountParent accountParent  = parentsInDatabase.get(0);
        assertThat(parentsInDatabase, hasSize(1));
        assertThat(accountParent.getAccountDetails().getUsername(),equalTo("Firstname.LastnameRO1"));
        assertThat(accountParent.getFirstname(),equalTo("Firstname"));
        assertThat(accountParent.getLastname(), equalTo("Lastname"));
        assertThat(accountParent.getPhoneNumber(),equalTo("111222333"));
    }

    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,TEACHER_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test add student from csv file with roles without permissions return 403 HTTP status")
    void addStudentFromCSVFile_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        String filePath =  "src/test/resources/csv_generator_test/test_adding_student.csv";
        String[] data = {
                "Lukasz,Szwacz,2021,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        };

        new File(filePath).createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,false));
        writer.write("Imie,Nazwisko,Rok,Nazwa klasy,Numer do rodzica" + "\n");
        for(String line : data) {
            writer.write(line + "\n");
        }
        writer.close();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/account/students")
                .cookie(egradeJWTCookie)
                .param("pathname",filePath)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
        new File(filePath).delete();
    }

    @Test
    @DisplayName("Test add student from csv file with ADMIN role return 201 HTTP status and add students and parents to database")
    void addStudentFromCSVFile_givenAdminRole_shouldReturn_201_HTTPStatus_andAddStudentsAndParentsToDatabase() throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,ADMIN_JWT_TOKEN);
        String filePath =  "src/test/resources/csv_generator_test/test_adding_student.csv";
        String[] data = {
                "Lukasz,Szwacz,2021,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        };

        new File(filePath).createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,false));
        writer.write("Imie,Nazwisko,Rok,Nazwa klasy,Numer do rodzica" + "\n");
        for(String line : data) {
            writer.write(line + "\n");
        }
        writer.close();


        //when
        ResultActions resultActions  = mockMvc.perform(post("/account/students")
                .cookie(egradeJWTCookie)
                .param("pathname",filePath)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        assertThat(accountStudentRepository.findAll(),hasSize(4));
        assertThat(accountParentRepository.findAll(),hasSize(4));
        new File(filePath).delete();
    }

    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,TEACHER_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test add teacher with roles without permissions return 403 HTTP status")
    void addTeacher_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        AccountTeacherRequestDTO accountTeacherRequestDTO = AccountTeacherRequestDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .subject("PHYSICS")
                .email("email@wp.pl")
                .workPhone("111222333")
                .build();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/account/teacher")
                .cookie(egradeJWTCookie)
                .content(objectMapper.writeValueAsString(accountTeacherRequestDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test add teacher with ADMIN role return 201 HTTP status and should save student to database")
    void addTeacher_givenAdminRole_shouldReturn_201_HTTPStatus_andCreateTeacherAccountWithProperFields() throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,ADMIN_JWT_TOKEN);
        AccountTeacherRequestDTO accountTeacherRequestDTO = AccountTeacherRequestDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .subject("PHYSICS")
                .email("email@wp.pl")
                .workPhone("111222333")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/account/teacher")
                .cookie(egradeJWTCookie)
                .content(objectMapper.writeValueAsString(accountTeacherRequestDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));

        List<AccountTeacher> teachersInDatabase = accountTeacherRepository.findAll();
        AccountTeacher accountTeacher = teachersInDatabase.get(0);
        assertThat(teachersInDatabase, hasSize(1));
        assertThat(accountTeacher.getFirstname(),is(equalTo(accountTeacherRequestDTO.firstname())));
        assertThat(accountTeacher.getLastname(),is(equalTo(accountTeacherRequestDTO.lastname())));
        assertThat(accountTeacher.getAccountDetails().getUsername(),is(equalTo("Firstname.LastnameNAU1")));
        assertThat(accountTeacher.getWorkPhone(),is(equalTo(accountTeacherRequestDTO.workPhone())));
        assertThat(accountTeacher.getEmail(),is(equalTo(accountTeacherRequestDTO.email())));
        assertThat(accountTeacher.getSubject(),is(equalTo(accountTeacherRequestDTO.subject())));
    }

    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,TEACHER_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test get account by id with roles without permissions return 403 HTTP status")
    void getAccountById_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        AccountStudent accountStudent = AccountStudent
                .builder()
                .accountDetails(AccountDetails
                        .builder()
                        .username("username")
                        .password(egradePasswordEncoder.encode("password"))
                        .accountRole(AccountRole.STUDENT)
                        .build())
                .firstname("firstname")
                .lastname("lastname")
                .schoolClassName("1A")
                .schoolClassYear(2023)
                .build();
        accountStudentRepository.save(accountStudent);
        int id = accountStudentRepository.findAll().get(0).getId();
        AccountRole accountRole = AccountRole.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(get("/account/"+ accountRole +"="+id)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test get account by id with ADMIN role return 201 HTTP status and should save student to database")
    void getAccountById_givenAdminRole_shouldReturn_201_HTTPStatus_andReturnStudentWithCorrectFields() throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,ADMIN_JWT_TOKEN);
        AccountStudent accountStudent = AccountStudent
                .builder()
                .accountDetails(AccountDetails
                        .builder()
                        .username("username")
                        .password(egradePasswordEncoder.encode("password"))
                        .accountRole(AccountRole.STUDENT)
                        .build())
                .firstname("firstname")
                .lastname("lastname")
                .schoolClassName("1A")
                .schoolClassYear(2023)
                .build();
        accountStudentRepository.save(accountStudent);
        int id = accountStudentRepository.findAll().get(0).getId();
        AccountRole accountRole = AccountRole.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(get("/account/"+ accountRole +"="+id)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        AccountResponseDTO expectedAccountResponseDTO = AccountResponseDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .accountType(AccountRole.STUDENT.toString())
                .id(id)
                .username("username")
                .password("password")
                .build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(expectedAccountResponseDTO)))));
    }

    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,TEACHER_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test delete account by id with roles without permissions return 403 HTTP status")
    void deleteAccountById_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        AccountStudent accountStudent = AccountStudent
                .builder()
                .accountDetails(AccountDetails
                        .builder()
                        .username("username")
                        .password(egradePasswordEncoder.encode("password"))
                        .accountRole(AccountRole.STUDENT)
                        .build())
                .firstname("firstname")
                .lastname("lastname")
                .schoolClassName("1A")
                .schoolClassYear(2023)
                .build();
        accountStudentRepository.save(accountStudent);
        int id = accountStudentRepository.findAll().get(0).getId();
        AccountRole accountRole = AccountRole.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(delete("/account/"+ accountRole +"="+id)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test delete account by id with ADMIN role return 201 HTTP status and should save student to database")
    void deleteAccountById_givenAdminRole_shouldReturn_201_HTTPStatus_andDeleteAccountFromDatabase() throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,ADMIN_JWT_TOKEN);
        AccountStudent accountStudent = AccountStudent
                .builder()
                .accountDetails(AccountDetails
                        .builder()
                        .username("username")
                        .password(egradePasswordEncoder.encode("password"))
                        .accountRole(AccountRole.STUDENT)
                        .build())
                .firstname("firstname")
                .lastname("lastname")
                .schoolClassName("1A")
                .schoolClassYear(2023)
                .build();
        accountStudentRepository.save(accountStudent);
        int id = accountStudentRepository.findAll().get(0).getId();
        AccountRole accountRole = AccountRole.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(delete("/account/"+ accountRole +"="+id)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        AccountResponseDTO expectedAccountResponseDTO = AccountResponseDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .accountType(AccountRole.STUDENT.toString())
                .id(id)
                .username("username")
                .password("password")
                .build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(expectedAccountResponseDTO)))));
        assertThat(accountStudentRepository.findById(id),is(Optional.empty()));
    }

    @Test
    @DisplayName("Test change account password with any role return 200 HTTP status and should change password")
    void changeAccountPassword_givenAdminRole_shouldReturn_200_HTTPStatus_andDeleteAccountFromDatabase() throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,ADMIN_JWT_TOKEN);
        String passwordInDatabase = "password";
        String newPassword = "Password1.";
        AccountStudent accountStudent = AccountStudent
                .builder()
                .accountDetails(AccountDetails
                        .builder()
                        .username("username")
                        .password(egradePasswordEncoder.encode("password"))
                        .accountRole(AccountRole.STUDENT)
                        .build())
                .firstname("firstname")
                .lastname("lastname")
                .schoolClassName("1A")
                .schoolClassYear(2023)
                .build();
        accountStudentRepository.save(accountStudent);
        int id = accountStudentRepository.findAll().get(0).getId();
        AccountRole accountRole = AccountRole.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(put("/account/password/"+ accountRole +"="+id)
                .cookie(egradeJWTCookie)
                .param("oldPassword",passwordInDatabase)
                .param("newPassword",newPassword)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        AccountResponseDTO expectedAccountResponseDTO = AccountResponseDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .accountType(AccountRole.STUDENT.toString())
                .id(id)
                .username("username")
                .password(newPassword)
                .build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(expectedAccountResponseDTO)))));
    }













}
