package com.github.starwacki.components.grades;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.grades.dto.GradeRequestDTO;
import com.github.starwacki.components.grades.dto.GradeResponeDTO;
import com.github.starwacki.components.grades.dto.SubjectResponseDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GradesControllerIntegrationTest {

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
    private GradeRepository gradeRepository;
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
    @DisplayName("Test add grade to student class with roles without permissions return 403 HTTP status")
    void addGradeToStudent_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        GradeRequestDTO gradeRequestDTO = GradeRequestDTO
                .builder()
                .addedBy("Szymon Kawka")
                .subject(GradeSubject.PHYSICS.toString())
                .weight(5)
                .description("Test from term test")
                .degree("6")
                .studentID(1)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/grades/grade")
                .cookie(egradeJWTCookie)
                .content(objectMapper.writeValueAsString(gradeRequestDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN_JWT_TOKEN,TEACHER_JWT_TOKEN})
    @DisplayName("Test add grade to student with ADMIN or TEACHER role return 201 HTTP status and add grade to database")
    void addGradeToStudent_givenAdminOrTeacherRole_shouldReturn_201_HTTPStatus_andAddGradeToDatabase(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        int studentId = 1;
        GradeRequestDTO gradeRequestDTO = GradeRequestDTO
                .builder()
                .addedBy("Szymon Kawka")
                .subject(GradeSubject.PHYSICS.toString())
                .weight(5)
                .description("Test from term test")
                .degree("6")
                .studentID(studentId)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/grades/grade")
                .cookie(egradeJWTCookie)
                .content(objectMapper.writeValueAsString(gradeRequestDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        List<Grade> gradeList = gradeRepository.findAllByStudentID(studentId).get();
        assertThat(gradeList,hasSize(1));
        Grade expectedGradeInDatabase = gradeList.get(0);
        assertThat(expectedGradeInDatabase.getStudentID(),is(equalTo(gradeRequestDTO.studentID())));
        assertThat(expectedGradeInDatabase.getGradeSubject().toString(),is(equalTo(gradeRequestDTO.subject())));
        assertThat(expectedGradeInDatabase.getWeight(),is(equalTo(gradeRequestDTO.weight())));
        assertThat(expectedGradeInDatabase.getGradeSymbolValue().getSymbol(),is(equalTo(gradeRequestDTO.degree())));
        assertThat(expectedGradeInDatabase.getAddedBy(),is(equalTo(gradeRequestDTO.addedBy())));
        assertThat(expectedGradeInDatabase.getDescription(),is(equalTo(gradeRequestDTO.description())));
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN_JWT_TOKEN,TEACHER_JWT_TOKEN,STUDENT_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test get student grade with any role return 200 HTTP status")
    void getStudentGrade_givenAnyRole_shouldReturn_200_HTTPStatus_andGradeViewDTOInResponseBody(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        int studentID = 1;
        Grade grade = Grade
                .builder()
                .gradeSubject(GradeSubject.PHYSICS)
                .studentID(studentID)
                .weight(5)
                .gradeSymbolValue(GradeSymbolValue.SIX)
                .addedDate(LocalDate.of(2023,12,10))
                .description("Grade from term test")
                .addedBy("firstname lastname")
                .build();
        int gradeId = gradeRepository.save(grade).getId();

        //when
        ResultActions resultActions = mockMvc.perform(get("/grades/student="+ studentID+"/"+gradeId)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        GradeResponeDTO expectedGradeInResponseBody = GradeResponeDTO
                .builder()
                .weight(5)
                .addedBy("firstname lastname")
                .addedDate(LocalDate.of(2023,12,10))
                .degree("6")
                .description("Grade from term test")
                .build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(expectedGradeInResponseBody)))));
    }

    @ParameterizedTest
    @ValueSource(strings = {STUDENT_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test delete student grade with roles without permissions return 403 HTTP status")
    void deleteStudentGrade_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        int studentID = 1;
        Grade grade = Grade
                .builder()
                .gradeSubject(GradeSubject.PHYSICS)
                .studentID(studentID)
                .weight(5)
                .gradeSymbolValue(GradeSymbolValue.SIX)
                .addedDate(LocalDate.of(2023,12,10))
                .description("Grade from term test")
                .addedBy("firstname lastname")
                .build();
        int gradeId = gradeRepository.save(grade).getId();

        //when
        ResultActions resultActions = mockMvc.perform(delete("/grades/student="+ studentID+"/"+gradeId)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN_JWT_TOKEN,TEACHER_JWT_TOKEN})
    @DisplayName("Test delete student grade with ADMIN or TEACHER role return 200 HTTP status and delete grade from database")
    void deleteStudentGrade_givenAdminOrTeacherRole_shouldReturn_200_HTTPStatus_andDeleteGradeFromDatabase(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        int studentID = 1;
        Grade grade = Grade
                .builder()
                .gradeSubject(GradeSubject.PHYSICS)
                .studentID(studentID)
                .weight(5)
                .gradeSymbolValue(GradeSymbolValue.SIX)
                .addedDate(LocalDate.of(2023,12,10))
                .description("Grade from term test")
                .addedBy("firstname lastname")
                .build();
        int gradeId = gradeRepository.save(grade).getId();

        //when
        ResultActions resultActions = mockMvc.perform(delete("/grades/student="+ studentID+"/"+gradeId)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        assertThat(gradeRepository.findByStudentIDAndId(studentID,gradeId),
                is(equalTo(Optional.empty())));
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN_JWT_TOKEN,TEACHER_JWT_TOKEN,STUDENT_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test get student grades with any role return 200 HTTP status and grades in response body")
    void getStudentGrades_givenAnyRole_shouldReturn_200_HTTPStatus_andReturnGradesInResponseBody(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);
        int studentID = 1;

        //when
        ResultActions resultActions = mockMvc.perform(get("/grades/student="+studentID)
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
       List<SubjectResponseDTO> expectedSubjectResponseDTOList = new ArrayList<>();
        for (GradeSubject gradeSubject : GradeSubject.values()) {
            expectedSubjectResponseDTOList.add(SubjectResponseDTO
                    .builder()
                            .subject(gradeSubject.toString())
                            .gradeAverage("0,00")
                            .grades(List.of())
                    .build());
        }
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains(objectMapper.writeValueAsString(expectedSubjectResponseDTOList))));
    }

    @ParameterizedTest
    @ValueSource(strings = {ADMIN_JWT_TOKEN,TEACHER_JWT_TOKEN,STUDENT_JWT_TOKEN,PARENT_JWT_TOKEN})
    @DisplayName("Test get student subject grades with any role return 200 HTTP status and grades in response body")
    void  getStudentSubjectGrades_givenAnyRole_shouldReturn_200_HTTPStatus_andReturnGradesInResponseBody(String jwt) throws Exception {

        //given
        Cookie egradeJWTCookie = new Cookie(AUTH_COOKIE_NAME,jwt);

        int studentID = 1;
        GradeSubject subject = GradeSubject.PHYSICS;

        //when
        ResultActions resultActions = mockMvc.perform(get("/grades/student="+studentID+"/subject="+subject.ordinal())
                .cookie(egradeJWTCookie)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        SubjectResponseDTO subjectResponseDTO = SubjectResponseDTO
                .builder()
                .subject(subject.toString())
                .gradeAverage("0,00")
                .grades(List.of())
                .build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains(objectMapper.writeValueAsString(subjectResponseDTO))));
    }




}
