package com.github.starwacki.components.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.components.student.exceptions.StudentNotFoundException;
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
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = StudentController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = OncePerRequestFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class StudentControllerUnitTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentFacade studentFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDTO prepareStudentDTO(String className, int year) {
        return StudentDTO
                .builder()
                .id(1)
                .firstname("firstname")
                .lastname("lastname")
                .parentPhone("111222333")
                .className(className)
                .year(year)
                .build();
    }


    @Test
    @DisplayName("Test get student from class return 200 HTTP status and list of students in body")
    void getAllStudentsFromClass_givenClassNameAndClassYear_shouldReturn_200_HTTPStatus_andListOfStudentsDtoInBody() throws Exception {
        //given
        int classYear = 2023;
        String className = "2A";
        List<StudentDTO> studentDTOS = List.of(
                prepareStudentDTO(className,classYear),
                prepareStudentDTO(className,classYear),
                prepareStudentDTO(className,classYear));
        given(studentFacade.getAllStudentsFromClass(className,classYear)).willReturn(studentDTOS);

        //when
        ResultActions response = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(studentDTOS);
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> assertThat(response.andReturn().getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1a","11","A1","KLASA","1AA","1aA"})
    @DisplayName("Test get students from class giving invalid class name return 400 HTTP status and error message")
    void getAllStudentsFromClass_givenInvalidClassName_shouldReturn_400_HTTPStatus_andErrorMessageInBody(String input) throws Exception {
        //given
        int classYear = 2023;
        String className = input;

        //when
        ResultActions result = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"className\":\"Invalid value: " + className + "\"}";
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,30,20,2019,2041,(-5)})
    @DisplayName("Test get students from class giving invalid class year return 400 HTTP status and error message")
    void getAllStudentsFromClass_givenInvalidYear_shouldReturn_400_HTTPStatus_andErrorMessageInBody(int input) throws Exception {
        //given
        int classYear = input;
        String className = "1A";

        //when
        ResultActions result = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"classYear\":\"Invalid value: " + classYear + "\"}";
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test get students from class giving year bigger than int range return 400 HTTP status and error message")
    void getAllStudentsFromClass_givenBiggerThanIntRange_shouldReturn_400_HTTPStatus_andErrorMessageInBody() throws Exception {
        //given
        int classYear = Integer.MAX_VALUE+1;
        String className = "1A";

        //when
        ResultActions result = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }


    @Test
    @DisplayName("Test get students from class giving invalid class name and year return 400 HTTP status and error message")
    void getAllStudentsFromClass_givenInvalidYearAndClassName_shouldReturn_400_HTTPStatus_andErrorMessageInBody() throws Exception {
        //given
        int classYear = -1000;
        String className = "1AA";

        //when
        ResultActions result = mockMvc.perform(get("/student/class="+className+"&year="+classYear)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"className\":\"Invalid value: "+ className + "\",\"classYear\":\"Invalid value: " + classYear + "\"}";
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test change student class when student not found return 404 HTTP status and error message")
    void changeStudentClass_givenNoExistStudentId_shouldReturn_404_HTTPStatus_andErrorMessageInBody() throws Exception {
        //given
        int studentID = 1;
        String className = "2A";
        int classYear = 2023;
        doThrow(new StudentNotFoundException(studentID)).when(studentFacade).changeStudentClass(studentID,className,classYear);

        //when
        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
                .param("className",className)
                .param("classYear",String.valueOf(classYear))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "Student not found id: " + studentID;
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result1 -> assertThat(result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test change student class give invalid className return 400 HTTP status and error message")
    void changeStudentClass_givenWrongClassName_shouldReturn_400_HTTPStatus_andErrorMessageInBody() throws Exception {
        //given
        int studentID = 1;
        String className = "2AA";
        int classYear = 2023;

        //when
        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
                .param("className",className)
                .param("classYear",String.valueOf(classYear))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"className\":\"Invalid value: " + className + "\"}";
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test change student class give invalid year return 400 HTTP status and error message")
    void changeStudentClass_givenWrongYear_shouldReturn_400_HTTPStatus_andErrorMessageInBody() throws Exception {
        //given
        int studentID = 1;
        String className = "2A";
        int classYear = 20231;

        //when
        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
                .param("className",className)
                .param("classYear",String.valueOf(classYear))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"classYear\":\"Invalid value: " + classYear + "\"}";
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test change student class give invalid year and name return 400 HTTP status and error message")
    void changeStudentClass_givenWrongYearAndClassName_shouldReturn_400_HTTPStatus_andErrorMessageInResponse() throws Exception {
        //given
        int studentID = 1;
        String className = "2AA";
        int classYear = 20231;

        //when
        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
                .param("className",className)
                .param("classYear",String.valueOf(classYear))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedErrorMessage = "{\"className\":\"Invalid value: "+ className + "\",\"classYear\":\"Invalid value: " + classYear + "\"}";
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }

    @Test
    @DisplayName("Test change student class give no exist student and invalid year and name return 400 HTTP status and error message")
    void changeStudentClass_givenNoExistStudentAndWrongYearAndClassName_shouldReturn_400_HTTPStatus_andErrorMessageInResponse() throws Exception {
        //given
        int studentID = 1;
        String className = "2AA";
        int classYear = 20231;


        //when
        ResultActions result = mockMvc.perform(put("/student/id="+studentID+"/class")
                .param("className",className)
                .param("classYear",String.valueOf(classYear))
                .contentType(MediaType.APPLICATION_JSON));
        doThrow(new StudentNotFoundException(studentID)).when(studentFacade).changeStudentClass(studentID,className,classYear);


        //then
        String expectedErrorMessage = "{\"className\":\"Invalid value: "+ className + "\",\"classYear\":\"Invalid value: " + classYear + "\"}";
        result
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result1 -> assertThat(
                        result1.getResponse().getContentAsString(),
                        is(equalTo(expectedErrorMessage))));
    }


}
