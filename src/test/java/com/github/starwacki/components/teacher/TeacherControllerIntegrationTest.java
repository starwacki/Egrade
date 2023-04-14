package com.github.starwacki.components.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.teacher.dto.TeacherResponseDTO;
import com.github.starwacki.components.teacher.dto.TeacherSchoolClassDTO;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TeacherControllerIntegrationTest {

    private static final String AUTH_COOKIE_NAME = "egrade-jwt";
    private static final String STUDENT_JWT_TOKEN = " eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTVFVERU5UIiwiaWF0IjoxNjgxNDA2OTMyLCJleHAiOjI1NDU0MDY5MzJ9.tSP-VwINn-uWEjoX_nCrLLzJeGPzexCnSZu2Ss3RC5k";
    private static final String TEACHER_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJURUFDSEVSIiwiaWF0IjoxNjgxNDA2OTMyLCJleHAiOjI1NDU0MDY5MzJ9.W6-Do2H5XNq8lBU_8y47hIUA4-BcypemWBS5HYb90Hg";
    private static final String PARENT_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQQVJFTlQiLCJpYXQiOjE2ODE0MDY5MzIsImV4cCI6MjU0NTQwNjkzMn0.ZHDI0HKH2anwf9VTwawu6EygcqvQI90Y4qQpXGb3CwE";
    private static final String ADMIN_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBRE1JTiIsImlhdCI6MTY4MTQwNjkzMiwiZXhwIjoyNTQ1NDA2OTMyfQ.IgsgZz42Ba8SxlIav5eBFbdqGnym8GswFPwOHp6QrsQ";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeacherRepository teacherRepository;
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

    @BeforeEach
    void prepareData() {
        TeacherSchoolClass schoolClass = TeacherSchoolClass.builder()
                .className("1A").classYear(2023).build();
        Set<TeacherSchoolClass> teacherSchoolClassSet = Set.of(schoolClass);
        Teacher accountTeacher = Teacher
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .email("email@wp.pl")
                .workPhone("111222333")
                .subject("PHYSICS")
                .teacherSchoolClass(teacherSchoolClassSet)
                .build();

        teacherRepository.save(accountTeacher);
    }

    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,PARENT_JWT_TOKEN,ADMIN_JWT_TOKEN,TEACHER_JWT_TOKEN})
    @DisplayName("Test get teacher classes return 200 HTTP status and teacher classes in responseBody")
    void  getTeacherClasses_givenTeacherId_shouldReturn_200_HTTPStatus_andTeacherClassesInResponseBody(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        int teacherId = teacherRepository.findAll().get(0).getId();

        //when
        ResultActions resultActions  = mockMvc.perform(get("/teacher/id="+teacherId+"/classes")
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(
               Set.of(TeacherSchoolClassDTO
                       .builder()
                       .className("1A")
                       .year(2023)
                       .build()));
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));

    }

    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,PARENT_JWT_TOKEN,TEACHER_JWT_TOKEN})
    @DisplayName("Test add school class to teacher with roles without permissions return 403 HTTP status")
    void  addSchoolClassToTeacher_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        int teacherId = teacherRepository.findAll().get(0).getId();
        TeacherSchoolClassDTO schoolClassDTO = TeacherSchoolClassDTO.builder()
                .year(2025)
                .className("3A").build();

        //when
        ResultActions resultActions  = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
                        .cookie(egradeJWTCookie)
                        .content(objectMapper.writeValueAsString(schoolClassDTO))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));

    }

    @Test
    @DisplayName("Test add school class to teacher with ADMIN role return 204 HTTP status and add class to teacher")
    @WithMockUser(authorities = "ADMIN")
    void  addSchoolClassToTeacher_givenAdminRole_shouldReturn_204_HTTPStatus_andAddClassToTeacher() throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,ADMIN_JWT_TOKEN);
        int teacherId = teacherRepository.findAll().get(0).getId();
        TeacherSchoolClassDTO schoolClassDTO = TeacherSchoolClassDTO.builder()
                .year(2025)
                .className("3A").build();

        //when
        ResultActions resultActions  = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
                .cookie(egradeJWTCookie)
                .content(objectMapper.writeValueAsString(schoolClassDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        TeacherSchoolClass expectedNewSchoolClass = TeacherSchoolClass.builder().classYear(2025).className("3A").build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()));
        Set<TeacherSchoolClass> actualTeacherClasses = teacherRepository.findTeacherById(teacherId).get().getTeacherSchoolClass();
        assertTrue(actualTeacherClasses.contains(expectedNewSchoolClass));
        assertThat(actualTeacherClasses,hasSize(2));
    }

    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,PARENT_JWT_TOKEN,ADMIN_JWT_TOKEN,TEACHER_JWT_TOKEN})
    @DisplayName("Test get all teachers information with any role return 200 HTTP status informations in response body")
    void   getAllTeachersInformation_shouldReturn_200_HTTPStatus_InformationAboutTeacher(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);

        //when
        ResultActions resultActions  = mockMvc.perform(get("/teacher/teachers")
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        List<TeacherResponseDTO> expectedTeachersDTO = List.of(
                TeacherResponseDTO
                        .builder()
                        .firstname("firstname")
                        .lastname("lastname")
                        .subject("PHYSICS")
                        .email("email@wp.pl")
                        .phone("111222333")
                        .build()
        );

        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(expectedTeachersDTO)))));
    }


}
