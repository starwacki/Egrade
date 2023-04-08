package com.github.starwacki.components.grades;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.grades.dto.GradeDTO;
import com.github.starwacki.components.grades.dto.GradeViewDTO;
import com.github.starwacki.components.grades.dto.SubjectDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GradeRepository gradeRepository;

    @Test
    @DisplayName("Test add grade to student class with roles without permissions return 403 HTTP status")
    @WithMockUser(authorities = {"PARENT","STUDENT"})
    void addGradeToStudent_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {


        GradeDTO gradeDTO = GradeDTO
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
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test add grade to student with ADMIN or TEACHER role return 201 HTTP status and add grade to database")
    @WithMockUser(authorities = {"ADMIN","TEACHER"})
    void addGradeToStudent_givenAdminOrTeacherRole_shouldReturn_201_HTTPStatus_andAddGradeToDatabase() throws Exception {

        //given
        int studentId = 1;
        GradeDTO gradeDTO = GradeDTO
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
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        List<Grade> gradeList = gradeRepository.findAllByStudentID(studentId).get();
        assertThat(gradeList,hasSize(1));
        Grade expectedGradeInDatabase = gradeList.get(0);
        assertThat(expectedGradeInDatabase.getStudentID(),is(equalTo(gradeDTO.studentID())));
        assertThat(expectedGradeInDatabase.getGradeSubject().toString(),is(equalTo(gradeDTO.subject())));
        assertThat(expectedGradeInDatabase.getWeight(),is(equalTo(gradeDTO.weight())));
        assertThat(expectedGradeInDatabase.getGradeSymbolValue().getSymbol(),is(equalTo(gradeDTO.degree())));
        assertThat(expectedGradeInDatabase.getAddedBy(),is(equalTo(gradeDTO.addedBy())));
        assertThat(expectedGradeInDatabase.getDescription(),is(equalTo(gradeDTO.description())));
    }

    @Test
    @DisplayName("Test get student grade with any role return 200 HTTP status")
    @WithMockUser(authorities = {"PARENT","STUDENT","ADMIN","TEACHER"})
    void getStudentGrade_givenAnyRole_shouldReturn_200_HTTPStatus_andGradeViewDTOInResponseBody() throws Exception {

        //given
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
                .contentType(MediaType.APPLICATION_JSON));

        //then
        GradeViewDTO expectedGradeInResponseBody = GradeViewDTO
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

    @Test
    @DisplayName("Test delete student grade with roles without permissions return 403 HTTP status")
    @WithMockUser(authorities = {"PARENT","STUDENT"})
    void deleteStudentGrade_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {

        //given
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
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test delete student grade with ADMIN or TEACHER role return 200 HTTP status and delete grade from database")
    @WithMockUser(authorities = {"ADMIN","TEACHER"})
    void deleteStudentGrade_givenAdminOrTeacherRole_shouldReturn_200_HTTPStatus_andDeleteGradeFromDatabase() throws Exception {

        //given
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
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        assertThat(gradeRepository.findByStudentIDAndId(studentID,gradeId),
                is(equalTo(Optional.empty())));
    }

    @Test
    @DisplayName("Test get student grades with any role return 200 HTTP status and grades in response body")
    @WithMockUser(authorities = {"PARENT","STUDENT","ADMIN","TEACHER"})
    void getStudentGrades_givenAnyRole_shouldReturn_200_HTTPStatus_andReturnGradesInResponseBody() throws Exception {

        //given
        int studentID = 1;

        //when
        ResultActions resultActions = mockMvc.perform(get("/grades/student="+studentID)
                .contentType(MediaType.APPLICATION_JSON));

        //then
       List<SubjectDTO> expectedSubjectDTOList = new ArrayList<>();
        for (GradeSubject gradeSubject : GradeSubject.values()) {
            expectedSubjectDTOList.add(SubjectDTO
                    .builder()
                            .subject(gradeSubject.toString())
                            .gradeAverage("0,00")
                            .grades(List.of())
                    .build());
        }
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains(objectMapper.writeValueAsString(expectedSubjectDTOList))));
    }

    @Test
    @DisplayName("Test get student subject grades with any role return 200 HTTP status and grades in response body")
    @WithMockUser(authorities = {"PARENT","STUDENT","ADMIN","TEACHER"})
    void  getStudentSubjectGrades_givenAnyRole_shouldReturn_200_HTTPStatus_andReturnGradesInResponseBody() throws Exception {

        //given
        int studentID = 1;
        GradeSubject subject = GradeSubject.PHYSICS;

        //when
        ResultActions resultActions = mockMvc.perform(get("/grades/student="+studentID+"/subject="+subject.ordinal())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        SubjectDTO subjectDTO = SubjectDTO
                .builder()
                .subject(subject.toString())
                .gradeAverage("0,00")
                .grades(List.of())
                .build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains(objectMapper.writeValueAsString(subjectDTO))));
    }




}
