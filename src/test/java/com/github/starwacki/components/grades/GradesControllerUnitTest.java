package com.github.starwacki.components.grades;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.grades.dto.GradeDTO;
import com.github.starwacki.components.grades.dto.GradeViewDTO;
import com.github.starwacki.components.grades.dto.SubjectDTO;
import com.github.starwacki.components.grades.exceptions.StudentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.filter.OncePerRequestFilter;
import java.time.LocalDate;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(controllers = GradesController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = OncePerRequestFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class GradesControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeFacade gradeFacade;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Test add grade to student when student no exist return 404 HTTP status and error message")
    void addGradeToStudent_giveNoExistStudentId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
        //given
        int studentID = 1;
        GradeDTO gradeDTO =  GradeDTO.builder()
                .weight(1)
                .studentID(studentID)
                .degree("5")
                .description("description")
                .subject(GradeSubject.PHYSICS.toString())
                .addedBy("Szymon Kawka")
                .build();
        given(gradeFacade.addGradeToStudent(gradeDTO)).willThrow(new StudentNotFoundException(studentID));


        //when
        ResultActions result = mockMvc.perform(post("/grades/grade")
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "Student not found id: " + studentID;
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),is(equalTo(expectedErrorMessage))));
    }


    @Test
    @DisplayName("Test add grade to student return 202 HTTP status and gradeDTO in response body")
    void addGradeToStudent_givenGradeDTO_shouldReturn_202_HTTPStatus_andResponseBodyWithGradeDTOSameLikeGivenDTO() throws Exception {
        //given
        int studentID = 1;
        GradeDTO gradeDTO =  GradeDTO.builder()
                .weight(1)
                .studentID(studentID)
                .degree("5")
                .description("description")
                .subject(GradeSubject.PHYSICS.toString())
                .addedBy("Szymon Kawka")
                .build();
        given(gradeFacade.addGradeToStudent(gradeDTO)).willReturn(gradeDTO);

        //when
        ResultActions result = mockMvc.perform(post("/grades/grade")
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(gradeDTO);
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @ValueSource(ints = {0,101,-5})
    @DisplayName("Test  validation gradeDto weight is invalid return 400 HTTP status and error message")
    void addGradeToStudent_givenInvalidGradeWeight_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage(int weight) throws Exception {
        //given
        int studentID = 1;
        GradeDTO gradeDTO =  GradeDTO.builder()
                .weight(weight)
                .studentID(studentID)
                .degree("5")
                .description("description")
                .subject(GradeSubject.PHYSICS.toString())
                .addedBy("Szymon Kawka")
                .build();
        given(gradeFacade.addGradeToStudent(gradeDTO)).willReturn(gradeDTO);

        //when
        ResultActions result = mockMvc.perform(post("/grades/grade")
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));
        System.out.println(result.andReturn().getResponse().getContentAsString());

        //then
        String expectedErrorMessage = "{\"weight\":\"Invalid value: must be between 1 and 100\"}";
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-5","-1","0","0.5","1.2","2.1","5.7","6.1","10","11"})
    @DisplayName("Test  validation gradeDto degree is invalid return 400 HTTP status and error message")
    void addGradeToStudent_givenInvalidDegree_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage(String degree) throws Exception {
        //given
        int studentID = 1;
        GradeDTO gradeDTO =  GradeDTO.builder()
                .weight(1)
                .studentID(studentID)
                .degree(degree)
                .description("description")
                .subject(GradeSubject.PHYSICS.toString())
                .addedBy("Szymon Kawka")
                .build();

        //when
        ResultActions result = mockMvc.perform(post("/grades/grade")
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"degree\":\"Invalid value: Invalid grade value\"}";
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test validation gradeDto without fields return 400 HTTP status")
    void addGradeToStudent_givenInvalidGradeDtoFields_shouldReturn_400_HTTPStatus() throws Exception {
        //given
        int studentID = 1;
        GradeDTO gradeDTO =  GradeDTO.builder()
                .build();

        //when
        ResultActions result = mockMvc.perform(post("/grades/grade")
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Test get one student grade when student no exist return 404 HTTP status and error message")
    void  getStudentGrade_givenNoExistStudentIdOrNoExistGradeId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
        //given
        int studentID = 1;
        int gradeID = 2;
        given(gradeFacade.getOneGrade(studentID,gradeID)).willThrow(new StudentNotFoundException(studentID));

        //when
        ResultActions result = mockMvc.perform(get("/grades/student="+studentID+"/"+gradeID)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "Student not found id: " + studentID;
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test get one student grade return 200 HTTP status and gradeDTO in responseBody")
    void  getStudentGrade_givenStudentIdAndGradeId_shouldReturn_200_HTTPStatus_andResponseBodyWithGradeDTO() throws Exception {
        //given
        int studentID = 1;
        int gradeID = 2;
        GradeViewDTO gradeViewDTO = GradeViewDTO
                .builder()
                .degree("5")
                .weight(10)
                .addedDate(LocalDate.of(2022,10,12))
                .addedBy("Jan Kowalski")
                .description("Ocena z klasowki")
                .build();
        given(gradeFacade.getOneGrade(studentID,gradeID)).willReturn(gradeViewDTO);

        //when
        ResultActions result = mockMvc.perform(get("/grades/student="+studentID+"/"+gradeID)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(gradeViewDTO);
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @Test
    @DisplayName("Test update student grade when student or grade no exist return 404 HTTP status and error message")
    void updateStudentGrade_givenNoExistStudentIdOrNoExistGradeId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
        //given
        int studentID = 1;
        int gradeID = 2;
        GradeDTO gradeDTO =  GradeDTO.builder()
                .weight(1)
                .studentID(studentID)
                .degree("5")
                .description("description")
                .subject(GradeSubject.PHYSICS.toString())
                .addedBy("Szymon Kawka")
                .build();
        given(gradeFacade.updateGrade(studentID,gradeID,gradeDTO)).willThrow(new StudentNotFoundException(studentID));

        //when
        ResultActions result = mockMvc.perform(put("/grades/student="+studentID+"/"+gradeID)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "Student not found id: " + studentID;
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test update student grade return 200 HTTP status and updated gradeDTO in response body")
    void updateStudentGrade_givenStudentIdAndGradeIdAndGradeDTO_shouldReturn_200_HTTPStatus_andResponseBodyWithUpdatedDTO() throws Exception {
        //given
        int studentID = 1;
        int gradeID = 2;
        GradeDTO gradeDTO =  GradeDTO.builder()
                .weight(1)
                .studentID(studentID)
                .degree("5")
                .description("description")
                .subject(GradeSubject.PHYSICS.toString())
                .addedBy("Szymon Kawka")
                .build();
        given(gradeFacade.updateGrade(studentID, gradeID, gradeDTO)).willReturn(gradeDTO);

        //when
        ResultActions result = mockMvc.perform(put("/grades/student="+studentID+"/"+gradeID)
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(gradeDTO);
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @Test
    @DisplayName("Test delete student grade when student or grade no exist return 404 HTTP status and error message")
    void deleteStudentGrade_givenNoExistStudentIdOrNoExistGradeId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
        //given
        int studentID = 1;
        int gradeID = 2;
        given(gradeFacade.deleteStudentGrade(studentID,gradeID)).willThrow(new StudentNotFoundException(studentID));

        //when
        ResultActions result = mockMvc.perform(delete("/grades/student="+studentID+"/"+gradeID)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "Student not found id: " + studentID;
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test delete student grade return 200 HTTP status and deleted gradeDTO in response body")
    void deleteStudentGrade_givenStudentIdAndGradeIdAndGradeDTO_shouldReturn_200_HTTPStatus_andResponseBodyWithDeletedDTO() throws Exception {
        //given
        int studentID = 1;
        int gradeID = 2;
        GradeDTO gradeDTO =  GradeDTO.builder()
                .weight(1)
                .studentID(studentID)
                .degree("5")
                .description("description")
                .subject(GradeSubject.PHYSICS.toString())
                .addedBy("Szymon Kawka")
                .build();
        given(gradeFacade.deleteStudentGrade(studentID, gradeID)).willReturn(gradeDTO);


        //when
        ResultActions result = mockMvc.perform(delete("/grades/student="+studentID+"/"+gradeID)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(gradeDTO);
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    ////

    @Test
    @DisplayName("Test get student grades when student no exist return 404 HTTP status and error message")
    void getStudentGrades_givenNoExistStudentId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
        //given
        int studentID = 1;
        given(gradeFacade.getAllGradesByStudentID(studentID)).willThrow(new StudentNotFoundException(studentID));

        //when
        ResultActions result = mockMvc.perform(get("/grades/student="+studentID)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "Student not found id: " + studentID;
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test get student grades return 200 HTTP status and StudentGradesDTO in response body")
    void getStudentGrades_givenStudentId_shouldReturn_200_HTTPStatus_andResponseBodyWithStudentGradesDTO() throws Exception {
        //given
        int studentID = 1;
        List<SubjectDTO> subjectDTOList = List.of(
                SubjectDTO.builder()
                        .subject("MATH")
                        .gradeAverage("2,0")
                        .grades(List.of(GradeViewDTO.builder().build(), GradeViewDTO.builder().build())).build());
        given(gradeFacade.getAllGradesByStudentID(studentID)).willReturn(subjectDTOList);

        //when
        ResultActions result = mockMvc.perform(get("/grades/student="+studentID)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody =objectMapper.writeValueAsString(subjectDTOList);
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    /////

    @Test
    @DisplayName("Test get student subject grades when student no exist return 404 HTTP status and error message")
    void getStudentSubjectGrades_givenNoExistStudentId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
        //given
        int studentID = 1;
        int subjectID = GradeSubject.PHYSICS.ordinal();
        given(gradeFacade.getOneSubjectGrades(studentID,subjectID)).willThrow(new StudentNotFoundException(studentID));

        //when
        ResultActions result = mockMvc.perform(get("/grades/student="+studentID+"/subject="+subjectID)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "Student not found id: " + studentID;
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test get student subject grade return 200 HTTP status and StudentGradesDTO in response body")
    void getStudentSubjectGrades_givenStudentId_shouldReturn_200_HTTPStatus_andResponseBodyWithStudentGradesDTO() throws Exception {
        //given
        int studentID = 1;
        int subjectID = GradeSubject.PHYSICS.ordinal();
        SubjectDTO studentGradesDTO = SubjectDTO
                .builder()
                .gradeAverage("1,0")
                .subject(GradeSubject.PHYSICS.toString())
                .grades(List.of(GradeViewDTO
                        .builder()
                                .addedBy("Szymon Kawka")
                                .weight(5)
                                .addedDate(LocalDate.of(2023,12,10))
                                .description("Klasowka")
                                .degree("5-")
                        .build()))
                .build();
        given(gradeFacade.getOneSubjectGrades(studentID,subjectID)).willReturn(studentGradesDTO);

        //when
        ResultActions result = mockMvc.perform(get("/grades/student="+studentID+"/subject="+subjectID)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody =objectMapper.writeValueAsString(studentGradesDTO);
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }


}
