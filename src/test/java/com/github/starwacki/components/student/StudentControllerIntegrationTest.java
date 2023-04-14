package com.github.starwacki.components.student;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.student.dto.StudentDTO;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StudentControllerIntegrationTest {

    private static final String AUTH_COOKIE_NAME = "egrade-jwt";
    private static final String STUDENT_JWT_TOKEN = " eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTVFVERU5UIiwiaWF0IjoxNjgxNDA2OTMyLCJleHAiOjI1NDU0MDY5MzJ9.tSP-VwINn-uWEjoX_nCrLLzJeGPzexCnSZu2Ss3RC5k";
    private static final String TEACHER_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJURUFDSEVSIiwiaWF0IjoxNjgxNDA2OTMyLCJleHAiOjI1NDU0MDY5MzJ9.W6-Do2H5XNq8lBU_8y47hIUA4-BcypemWBS5HYb90Hg";
    private static final String PARENT_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQQVJFTlQiLCJpYXQiOjE2ODE0MDY5MzIsImV4cCI6MjU0NTQwNjkzMn0.ZHDI0HKH2anwf9VTwawu6EygcqvQI90Y4qQpXGb3CwE";
    private static final String ADMIN_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBRE1JTiIsImlhdCI6MTY4MTQwNjkzMiwiZXhwIjoyNTQ1NDA2OTMyfQ.IgsgZz42Ba8SxlIav5eBFbdqGnym8GswFPwOHp6QrsQ";

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
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
    @ValueSource(strings = {STUDENT_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test get all students from class with roles without permissions return 403 HTTP status")
    void getAllStudentsFromClass_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        String className = "2A";
        int year = 2023;

        //when
        ResultActions resultActions = mockMvc.perform(get("/student/class="+className+"&year="+year)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN_JWT_TOKEN,TEACHER_JWT_TOKEN})
    @DisplayName("Test get all students from class with ADMIN role return 200 HTTP status and students in response body")
    void getAllStudentsFromClass_givenAdminOrTeacherRole_shouldReturn_200_HTTPStatus_andStudentsInResponseBody(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        String className = "2A";
        int year = 2023;
        Student accountStudent = Student.builder()
                .firstname("firstname1")
                .lastname("lastname1")
                .schoolClassName(className)
                .schoolClassYear(year)
                .parentPhoneNumber("111222333")
                .build();
        int savedId = studentRepository.save(accountStudent).getId();

        //when
        ResultActions resultActions = mockMvc.perform(get("/student/class="+className+"&year="+year)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        List<StudentDTO> expectedStudentsListInResponseBody = List.of(
                StudentDTO
                        .builder()
                        .id(savedId)
                        .className(className)
                        .parentPhone("111222333")
                        .year(year)
                        .firstname("firstname1")
                        .lastname("lastname1")
                        .build()
        );
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(expectedStudentsListInResponseBody)))));
    }

    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test change student class with roles without permissions return 403 HTTP status")
    void changeStudentClass_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        String newClassName = "1A";
        int newClassYear = 2025;
        Student accountStudent = Student.builder()
                .firstname("firstname")
                .lastname("lastname")
                .schoolClassName("2A")
                .schoolClassYear(2023)
                .parentPhoneNumber("111222333")
                .build();
        int savedId = studentRepository.save(accountStudent).getId();




        //when
        ResultActions resultActions = mockMvc.perform(put("/student/id="+savedId+"/class")
                .cookie(egradeJWTCookie)
                .param("className",newClassName)
                .param("classYear",String.valueOf(newClassYear))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN_JWT_TOKEN,TEACHER_JWT_TOKEN})
    @DisplayName("Test change student class with ADMIN or TEACHER role return 204 HTTP status and changed student class")
    void changeStudentClass_givenAdminOrTeacherRole_shouldReturn_204_HTTPStatus_andChangedStudentClass(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        String newClassName = "1A";
        int newClassYear = 2025;
        Student accountStudent = Student.builder()
                .firstname("firstname")
                .lastname("lastname")
                .schoolClassName("2A")
                .schoolClassYear(2023)
                .parentPhoneNumber("111222333")
                .build();
        int savedId = studentRepository.save(accountStudent).getId();




        //when
        ResultActions resultActions = mockMvc.perform(put("/student/id="+savedId+"/class")
                .cookie(egradeJWTCookie)
                .param("className",newClassName)
                .param("classYear",String.valueOf(newClassYear))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()));
        Student expectedStudentWithChangedClass = studentRepository.findById(savedId).get();
        assertThat(expectedStudentWithChangedClass.getSchoolClassName(),is(equalTo(newClassName)));
        assertThat(expectedStudentWithChangedClass.getSchoolClassYear(),is(equalTo(newClassYear)));
    }





}
