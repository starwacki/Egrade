package com.github.starwacki.components.teacher.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.teacher.dto.SchoolClassDTO;
import com.github.starwacki.components.teacher.dto.TeacherDTO;
import com.github.starwacki.components.teacher.exceptions.SchoolClassNotFoundException;
import com.github.starwacki.components.teacher.exceptions.TeacherNotFoundException;
import com.github.starwacki.components.teacher.service.TeacherService;
import com.github.starwacki.common.model.grades.Subject;
import com.github.starwacki.common.security.JwtAuthenticationFilter;
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
import static org.hamcrest.Matchers.*;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TeacherControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private TeacherService teacherService;


    @Test
    @DisplayName("Test get teacher classes return 200 HTTP status and list of school classes dto in response body")
    void getTeacherClasses_givenTeacherId_shouldReturn_200_HTTPStatus_andResponseBodyWithListOfSchoolClassesDTO() throws Exception {
        //given
        int teacherId = 1;
        List<SchoolClassDTO> listOfTeacherClasses = List.of(
                SchoolClassDTO
                        .builder()
                        .year(2023)
                        .className("2A")
                        .build(),
                SchoolClassDTO
                        .builder()
                        .year(2024)
                        .className("3A")
                        .build()
        );
        given(teacherService.getTeacherClasses(teacherId)).willReturn(listOfTeacherClasses);

        //when
        ResultActions response = mockMvc.perform(get("/teacher/id="+teacherId+"/classes")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(listOfTeacherClasses);
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(
                        result.getResponse().getContentAsString(), is(equalTo(expectedResponseBody))
                ));
    }

    @Test
    @DisplayName("Test add school class to teacher return 204 HTTP status")
    void addSchoolClassToTeacher_givenTeacherIdAndSchoolClassDto_shouldReturn_204_HTTPStatus() throws Exception {
        //given
        int teacherId = 1;
        SchoolClassDTO schoolClassDTO =   SchoolClassDTO
                .builder()
                .year(2024)
                .className("3A")
                .build();

        //when
        ResultActions response = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
                        .content(objectMapper.writeValueAsString(schoolClassDTO))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()));
        verify(teacherService).addSchoolClassToTeacher(teacherId,schoolClassDTO);
    }

    @Test
    @DisplayName("Test add school class to no exist teacher return 404 HTTP status and error message in response body")
    void addSchoolClassToTeacher_givenNoExistTeacherIdAndSchoolClassDto_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
        //given
        int teacherId = 1;
        SchoolClassDTO schoolClassDTO =   SchoolClassDTO
                .builder()
                .year(2024)
                .className("3A")
                .build();
        doThrow(new TeacherNotFoundException(teacherId)).when(teacherService).addSchoolClassToTeacher(teacherId,schoolClassDTO);


        //when
        ResultActions response = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
                .content(objectMapper.writeValueAsString(schoolClassDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "Not found teacher with id: " + teacherId;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))
                ));
    }

    @Test
    @DisplayName("Test add no exist school class to teacher return 404 HTTP status and error message in response body")
    void addSchoolClassToTeacher_givenTeacherIdAndNoExistSchoolClass_shouldReturn_404_HTTPStatus_andResponseBodyWithErrorMessage() throws Exception {
        //given
        int teacherId = 1;
        String className = "3A";
        int year = 2024;
        SchoolClassDTO schoolClassDTO =   SchoolClassDTO
                .builder()
                .year(year)
                .className(className)
                .build();
        doThrow(new SchoolClassNotFoundException(className,year)).when(teacherService).addSchoolClassToTeacher(teacherId,schoolClassDTO);


        //when
        ResultActions response = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
                .content(objectMapper.writeValueAsString(schoolClassDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = "Not found class with declared name and year: " + className + " " + year;
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))
                ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1a","11","A1","KLASA","1AA","1aA"})
    @DisplayName("Test add school class to teacher validation SchoolClassDTO return 400 HTTP status when wrong className")
    void addSchoolClassToTeacher_givenInvalidClassName_shouldReturn_400_HTTPStatus(String className) throws Exception {
        //given
        int teacherId = 1;
        SchoolClassDTO schoolClassDTO =   SchoolClassDTO
                .builder()
                .year(2024)
                .className(className)
                .build();

        //when
        ResultActions response = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
                .content(objectMapper.writeValueAsString(schoolClassDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,30,20,2019,2041,(-5)})
    @DisplayName("Test add school class to teacher validation SchoolClassDTO return 400 HTTP status when wrong year")
    void addSchoolClassToTeacher_givenInvalidYear_shouldReturn_400_HTTPStatus(int year) throws Exception {
        //given
        int teacherId = 1;
        SchoolClassDTO schoolClassDTO =   SchoolClassDTO
                .builder()
                .year(year)
                .className("3A")
                .build();

        //when
        ResultActions response = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
                .content(objectMapper.writeValueAsString(schoolClassDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Test get all teachers informations return 200 HTTP status and List of TeachersDTO in response body")
    void getAllTeachersInformation_shouldReturn200HttpStatusAndListOfTeacherDTO() throws Exception {
        //given
        TeacherDTO teacherDTO = TeacherDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .phone("111222333")
                .email("email@wp.pl")
                .subject(Subject.PHYSICS.toString())
                .build();
        List<TeacherDTO> listOfAllTeachers = List.of(teacherDTO);
        given(teacherService.getAllTeachers()).willReturn(listOfAllTeachers);

        //when
        ResultActions response = mockMvc.perform(get("/teacher/teachers")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        response
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(listOfAllTeachers)))));
    }
}
