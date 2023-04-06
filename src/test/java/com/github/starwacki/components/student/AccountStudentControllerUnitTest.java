package com.github.starwacki.components.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.student.StudentController;
import com.github.starwacki.components.student.dto.*;
import com.github.starwacki.components.student.exceptions.StudentNotFoundException;
import com.github.starwacki.components.student.exceptions.SubjectNotFoundException;
import com.github.starwacki.components.student.exceptions.TeacherAccountNotFoundException;
import com.github.starwacki.common.model.grades.Subject;
import com.github.starwacki.components.student.StudentGradeService;
import com.github.starwacki.components.student.StudentService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AccountStudentControllerUnitTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private StudentService studentService;
//
//    @MockBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @MockBean
//    private StudentGradeService studentGradeService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private StudentDTO prepareStudentDTO(String className,int year) {
//        return StudentDTO
//                .builder()
//                .id(1)
//                .firstname("firstname")
//                .lastname("lastname")
//                .parentPhone("111222333")
//                .className(className)
//                .year(year)
//                .build();
//    }
//
//
//    @Test
//    @DisplayName("Test get student from class return 200 HTTP status and list of students in body")
//    void getAllStudentsFromClass_givenClassNameAndClassYear_shouldReturn_200_HTTPStatus_andListOfStudentsDtoInBody() throws Exception {
//        //given
//        int classYear = 2023;
//        String className = "2A";
//        List<StudentDTO> studentDTOS = List.of(
//                prepareStudentDTO(className,classYear),
//                prepareStudentDTO(className,classYear),
//                prepareStudentDTO(className,classYear));
//        given(studentService.getAllStudentsFromClass(className,classYear)).willReturn(studentDTOS);
//
//        //when
//        ResultActions response = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedResponseBody = objectMapper.writeValueAsString(studentDTOS);
//        response
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(result -> assertThat(response.andReturn().getResponse().getContentAsString(),
//                        is(equalTo(expectedResponseBody))));
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"1a","11","A1","KLASA","1AA","1aA"})
//    @DisplayName("Test get students from class giving invalid class name return 400 HTTP status and error message")
//    void getAllStudentsFromClass_givenInvalidClassName_shouldReturn_400_HTTPStatus_andErrorMessageInBody(String input) throws Exception {
//        //given
//        int classYear = 2023;
//        String className = input;
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "{\"className\":\"Invalid value: " + className + "\"}";
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {1,30,20,2019,2041,(-5)})
//    @DisplayName("Test get students from class giving invalid class year return 400 HTTP status and error message")
//    void getAllStudentsFromClass_givenInvalidYear_shouldReturn_400_HTTPStatus_andErrorMessageInBody(int input) throws Exception {
//        //given
//        int classYear = input;
//        String className = "1A";
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "{\"classYear\":\"Invalid value: " + classYear + "\"}";
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test get students from class giving year bigger than int range return 400 HTTP status and error message")
//    void getAllStudentsFromClass_givenBiggerThanIntRange_shouldReturn_400_HTTPStatus_andErrorMessageInBody() throws Exception {
//        //given
//        int classYear = Integer.MAX_VALUE+1;
//        String className = "1A";
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        result.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
//    }
//
//
//    @Test
//    @DisplayName("Test get students from class giving invalid class name and year return 400 HTTP status and error message")
//    void getAllStudentsFromClass_givenInvalidYearAndClassName_shouldReturn_400_HTTPStatus_andErrorMessageInBody() throws Exception {
//        //given
//        int classYear = -1000;
//        String className = "1AA";
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "{\"className\":\"Invalid value: "+ className + "\",\"classYear\":\"Invalid value: " + classYear + "\"}";
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test change student class when student not found return 404 HTTP status and error message")
//    void changeStudentClass_givenNoExistStudentId_shouldReturn_404_HTTPStatus_andErrorMessageInBody() throws Exception {
//        //given
//        int studentID = 1;
//        String className = "2A";
//        int classYear = 2023;
//        doThrow(new StudentNotFoundException(studentID)).when(studentService).changeStudentClass(studentID,className,classYear);
//
//        //when
//        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
//                .param("className",className)
//                .param("classYear",String.valueOf(classYear))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "Student not found id: " + studentID;
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test change student class give invalid className return 400 HTTP status and error message")
//    void changeStudentClass_givenWrongClassName_shouldReturn_400_HTTPStatus_andErrorMessageInBody() throws Exception {
//        //given
//        int studentID = 1;
//        String className = "2AA";
//        int classYear = 2023;
//
//        //when
//        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
//                .param("className",className)
//                .param("classYear",String.valueOf(classYear))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "{\"className\":\"Invalid value: " + className + "\"}";
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test change student class give invalid year return 400 HTTP status and error message")
//    void changeStudentClass_givenWrongYear_shouldReturn_400_HTTPStatus_andErrorMessageInBody() throws Exception {
//        //given
//        int studentID = 1;
//        String className = "2A";
//        int classYear = 20231;
//
//        //when
//        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
//                .param("className",className)
//                .param("classYear",String.valueOf(classYear))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "{\"classYear\":\"Invalid value: " + classYear + "\"}";
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test change student class give invalid year and name return 400 HTTP status and error message")
//    void changeStudentClass_givenWrongYearAndClassName_shouldReturn_400_HTTPStatus_andErrorMessageInResponse() throws Exception {
//        //given
//        int studentID = 1;
//        String className = "2AA";
//        int classYear = 20231;
//
//        //when
//        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
//                .param("className",className)
//                .param("classYear",String.valueOf(classYear))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "{\"className\":\"Invalid value: "+ className + "\",\"classYear\":\"Invalid value: " + classYear + "\"}";
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test change student class give no exist student and invalid year and name return 400 HTTP status and error message")
//    void changeStudentClass_givenNoExistStudentAndWrongYearAndClassName_shouldReturn_400_HTTPStatus_andErrorMessageInResponse() throws Exception {
//        //given
//        int studentID = 1;
//        String className = "2AA";
//        int classYear = 20231;
//
//
//        //when
//        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
//                .param("className",className)
//                .param("classYear",String.valueOf(classYear))
//                .contentType(MediaType.APPLICATION_JSON));
//        doThrow(new StudentNotFoundException(studentID)).when(studentService).changeStudentClass(studentID,className,classYear);
//
//
//        //then
//        String expectedErrorMessage = "{\"className\":\"Invalid value: "+ className + "\",\"classYear\":\"Invalid value: " + classYear + "\"}";
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test add grade to student when student no exist return 404 HTTP status and error message")
//    void addGradeToStudent_giveNoExistStudentId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
//        //given
//        int studentID = 1;
//        GradeDTO gradeDTO =  GradeDTO.builder()
//                .weight(1)
//                .degree("5")
//                .description("description")
//                .subject(Subject.PHYSICS)
//                .addingTeacherId(1)
//                .build();
//        given(studentGradeService.addGradeToStudent(gradeDTO,studentID)).willThrow(new StudentNotFoundException(studentID));
//
//
//        //when
//        ResultActions result = mockMvc.perform(post("/student/id="+studentID+"/grade")
//                .content(objectMapper.writeValueAsString(gradeDTO))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "Student not found id: " + studentID;
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test add grade to student when adding teacher no exist return 404 HTTP status and error message")
//    void addGradeToStudent_giveNoExistTeacherId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
//        //given
//        int studentID = 1;
//        int teacherId = 2;
//        GradeDTO gradeDTO =  GradeDTO.builder()
//                .weight(1)
//                .degree("5")
//                .description("description")
//                .subject(Subject.PHYSICS)
//                .addingTeacherId(teacherId)
//                .build();
//        given(studentGradeService.addGradeToStudent(gradeDTO,studentID)).willThrow(new TeacherAccountNotFoundException(teacherId));
//
//
//        //when
//        ResultActions result = mockMvc.perform(post("/student/id="+studentID+"/grade")
//                .content(objectMapper.writeValueAsString(gradeDTO))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "Teacher not found id: " + teacherId;
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test add grade to student return 202 HTTP status and gradeDTO in response body")
//    void addGradeToStudent_givenGradeDTO_shouldReturn_202_HTTPStatus_andResponseBodyWithGradeDTOSameLikeGivenDTO() throws Exception {
//        //given
//        int studentID = 1;
//        GradeDTO gradeDTO =  GradeDTO.builder()
//                .weight(1)
//                .degree("5")
//                .description("description")
//                .subject(Subject.PHYSICS)
//                .addingTeacherId(1)
//                .build();
//        given(studentGradeService.addGradeToStudent(gradeDTO,studentID)).willReturn(gradeDTO);
//
//        //when
//        ResultActions result = mockMvc.perform(post("/student/id="+studentID+"/grade")
//                .content(objectMapper.writeValueAsString(gradeDTO))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedResponseBody = objectMapper.writeValueAsString(gradeDTO);
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedResponseBody))));
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {0,101,-5})
//    @DisplayName("Test  validation gradeDto weight is invalid return 400 HTTP status and error message")
//    void addGradeToStudent_givenInvalidGradeWeight_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage(int weight) throws Exception {
//        //given
//        int studentID = 1;
//        GradeDTO gradeDTO =  GradeDTO.builder()
//                .weight(weight)
//                .degree("5")
//                .description("description")
//                .subject(Subject.PHYSICS)
//                .addingTeacherId(1)
//                .build();
//        given(studentGradeService.addGradeToStudent(gradeDTO,studentID)).willReturn(gradeDTO);
//
//        //when
//        ResultActions result = mockMvc.perform(post("/student/id="+studentID+"/grade")
//                .content(objectMapper.writeValueAsString(gradeDTO))
//                .contentType(MediaType.APPLICATION_JSON));
//        System.out.println(result.andReturn().getResponse().getContentAsString());
//
//        //then
//        String expectedErrorMessage = "{\"weight\":\"Invalid value: must be between 1 and 100\"}";
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"-5","-1","0","0.5","1.2","2.1","5.7","6.1","10","11"})
//    @DisplayName("Test  validation gradeDto degree is invalid return 400 HTTP status and error message")
//    void addGradeToStudent_givenInvalidDegree_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage(String degree) throws Exception {
//        //given
//        int studentID = 1;
//        GradeDTO gradeDTO =  GradeDTO.builder()
//                .weight(1)
//                .degree(degree)
//                .description("description")
//                .subject(Subject.PHYSICS)
//                .addingTeacherId(1)
//                .build();
//
//        //when
//        ResultActions result = mockMvc.perform(post("/student/id="+studentID+"/grade")
//                .content(objectMapper.writeValueAsString(gradeDTO))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "{\"degree\":\"Invalid value: Invalid grade value\"}";
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
//                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test validation gradeDto without fields return 400 HTTP status")
//    void addGradeToStudent_givenInvalidGradeDtoFields_shouldReturn_400_HTTPStatus() throws Exception {
//        //given
//        int studentID = 1;
//        GradeDTO gradeDTO =  GradeDTO.builder()
//                .build();
//
//        //when
//        ResultActions result = mockMvc.perform(post("/student/id="+studentID+"/grade")
//                .content(objectMapper.writeValueAsString(gradeDTO))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
//    }
//
//    @Test
//    @DisplayName("Test get one student grade when student no exist return 404 HTTP status and error message")
//    void  getStudentGrade_givenNoExistStudentIdOrNoExistGradeId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
//        //given
//        int studentID = 1;
//        int gradeID = 2;
//        given(studentGradeService.getOneGrade(studentID,gradeID)).willThrow(new StudentNotFoundException(studentID));
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/id="+studentID+"/grade="+gradeID)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "Student not found id: " + studentID;
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test get one student grade return 200 HTTP status and gradeDTO in responseBody")
//    void  getStudentGrade_givenStudentIdAndGradeId_shouldReturn_200_HTTPStatus_andResponseBodyWithGradeDTO() throws Exception {
//        //given
//        int studentID = 1;
//        int gradeID = 2;
//        GradeViewDTO gradeViewDTO = GradeViewDTO
//                .builder()
//                .degree("5")
//                .weight(10)
//                .addedDate(LocalDate.of(2022,10,12))
//                .addedBy("Jan Kowalski")
//                .description("Ocena z klasowki")
//                .build();
//        given(studentGradeService.getOneGrade(studentID,gradeID)).willReturn(gradeViewDTO);
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/id="+studentID+"/grade="+gradeID)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedResponseBody = objectMapper.writeValueAsString(gradeViewDTO);
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedResponseBody))));
//    }
//
//    @Test
//    @DisplayName("Test update student grade when student or grade no exist return 404 HTTP status and error message")
//    void updateStudentGrade_givenNoExistStudentIdOrNoExistGradeId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
//        //given
//        int studentID = 1;
//        int gradeID = 2;
//        GradeDTO gradeDTO = GradeDTO.builder()
//                .weight(1)
//                .degree("2")
//                .description("description")
//                .subject(Subject.PHYSICS)
//                .addingTeacherId(1)
//                .build();
//        given(studentGradeService.updateGrade(studentID,gradeID,gradeDTO)).willThrow(new StudentNotFoundException(studentID));
//
//        //when
//        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/grade="+gradeID)
//                .content(objectMapper.writeValueAsString(gradeDTO))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "Student not found id: " + studentID;
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test update student grade return 200 HTTP status and updated gradeDTO in response body")
//    void updateStudentGrade_givenStudentIdAndGradeIdAndGradeDTO_shouldReturn_200_HTTPStatus_andResponseBodyWithUpdatedDTO() throws Exception {
//        //given
//        int studentID = 1;
//        int gradeID = 2;
//        GradeDTO gradeDTO = GradeDTO.builder()
//                .weight(1)
//                .degree("2")
//                .description("description")
//                .subject(Subject.PHYSICS)
//                .addingTeacherId(1)
//                .build();
//         given(studentGradeService.updateGrade(studentID, gradeID, gradeDTO)).willReturn(gradeDTO);
//
//        //when
//        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/grade="+gradeID)
//                .content(objectMapper.writeValueAsString(gradeDTO))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedResponseBody = objectMapper.writeValueAsString(gradeDTO);
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedResponseBody))));
//    }
//
//    @Test
//    @DisplayName("Test delete student grade when student or grade no exist return 404 HTTP status and error message")
//    void deleteStudentGrade_givenNoExistStudentIdOrNoExistGradeId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
//        //given
//        int studentID = 1;
//        int gradeID = 2;
//        given(studentGradeService.deleteStudentGrade(studentID,gradeID)).willThrow(new StudentNotFoundException(studentID));
//
//        //when
//        ResultActions result = mockMvc.perform(delete("/student/id="+studentID+"/grade="+gradeID)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "Student not found id: " + studentID;
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test delete student grade return 200 HTTP status and deleted gradeDTO in response body")
//    void deleteStudentGrade_givenStudentIdAndGradeIdAndGradeDTO_shouldReturn_200_HTTPStatus_andResponseBodyWithDeletedDTO() throws Exception {
//        //given
//        int studentID = 1;
//        int gradeID = 2;
//        GradeDTO gradeDTO = GradeDTO.builder()
//                .weight(1)
//                .degree("2")
//                .description("description")
//                .subject(Subject.PHYSICS)
//                .addingTeacherId(1)
//                .build();
//        given(studentGradeService.deleteStudentGrade(studentID, gradeID)).willReturn(gradeDTO);
//
//
//        //when
//        ResultActions result = mockMvc.perform(delete("/student/id="+studentID+"/grade="+gradeID)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedResponseBody = objectMapper.writeValueAsString(gradeDTO);
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedResponseBody))));
//    }
//
//    ////
//
//    @Test
//    @DisplayName("Test get student grades when student no exist return 404 HTTP status and error message")
//    void getStudentGrades_givenNoExistStudentId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
//        //given
//        int studentID = 1;
//        given(studentGradeService.getAllSubjectGradesByStudentID(studentID)).willThrow(new StudentNotFoundException(studentID));
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/id="+studentID+"/grades")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "Student not found id: " + studentID;
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test get student grades return 200 HTTP status and StudentGradesDTO in response body")
//    void getStudentGrades_givenStudentId_shouldReturn_200_HTTPStatus_andResponseBodyWithStudentGradesDTO() throws Exception {
//        //given
//        int studentID = 1;
//        StudentGradesDTO studentGradesDTO = StudentGradesDTO
//                .builder()
//                .className("2A")
//                .firstname("Jan")
//                .lastname("Kowalski")
//                .year(2022)
//                .subjectGrades(List.of(
//                        SubjectDTO
//                                .builder()
//                                .subject(Subject.PHYSICS)
//                                .gradeAverage("2,5")
//                                .grades(List.of(GradeViewDTO.builder()
//                                                .degree("5")
//                                                .addedDate(LocalDate.now())
//                                                .addedBy("Janusz Musial")
//                                                .description("Opis")
//                                                .weight(5)
//                                                .build())
//                                )
//                                .build()
//                ))
//                .build();
//        given(studentGradeService.getAllSubjectGradesByStudentID(studentID)).willReturn(studentGradesDTO);
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/id="+studentID+"/grades")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedResponseBody =objectMapper.writeValueAsString(studentGradesDTO);
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedResponseBody))));
//    }
//
//    /////
//
//    @Test
//    @DisplayName("Test get student subject grades when student no exist return 404 HTTP status and error message")
//    void getStudentSubjectGrades_givenNoExistStudentId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
//        //given
//        int studentID = 1;
//        int subjectID = Subject.PHYSICS.ordinal();
//        given(studentGradeService.getOneSubjectGrades(studentID,subjectID)).willThrow(new StudentNotFoundException(studentID));
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/id="+studentID+"/grades/subject="+subjectID)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "Student not found id: " + studentID;
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test get student subject grades when subject no exist return 404 HTTP status and error message")
//    void getStudentSubjectGrades_givenNoExistSubjectId_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
//        //given
//        int studentID = 1;
//        int subjectID = 111;
//        given(studentGradeService.getOneSubjectGrades(studentID,subjectID)).willThrow(new SubjectNotFoundException(subjectID));
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/id="+studentID+"/grades/subject="+subjectID)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedErrorMessage = "Subject not found, id: " + subjectID;
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedErrorMessage))));
//    }
//
//    @Test
//    @DisplayName("Test delete student subject grade return 200 HTTP status and StudentGradesDTO in response body")
//    void getStudentSubjectGrades_givenStudentId_shouldReturn_200_HTTPStatus_andResponseBodyWithStudentGradesDTO() throws Exception {
//        //given
//        int studentID = 1;
//        int subjectID = Subject.PHYSICS.ordinal();
//        StudentGradesDTO studentGradesDTO = StudentGradesDTO
//                .builder()
//                .className("2A")
//                .firstname("Jan")
//                .lastname("Kowalski")
//                .year(2022)
//                .subjectGrades(List.of(
//                        SubjectDTO
//                                .builder()
//                                .subject(Subject.PHYSICS)
//                                .gradeAverage("2,5")
//                                .grades(List.of(GradeViewDTO.builder()
//                                        .degree("5")
//                                        .addedDate(LocalDate.now())
//                                        .addedBy("Janusz Musial")
//                                        .description("Opis")
//                                        .weight(5)
//                                        .build())
//                                )
//                                .build()
//                ))
//                .build();
//        given(studentGradeService.getOneSubjectGrades(studentID,subjectID)).willReturn(studentGradesDTO);
//
//        //when
//        ResultActions result = mockMvc.perform(get("/student/id="+studentID+"/grades/subject="+subjectID)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedResponseBody =objectMapper.writeValueAsString(studentGradesDTO);
//        result
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
//                .andExpect(result1 -> assertThat(
//                        result1.getResponse().getContentAsString(),
//                        is(equalTo(expectedResponseBody))));
//    }
//

}
