package com.github.starwacki.components.student;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.student.dto.StudentDTO;
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

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Test get all students from class with roles without permissions return 403 HTTP status")
    @WithMockUser(authorities = {"PARENT","STUDENT"})
    void getAllStudentsFromClass_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {

        //given
        String className = "2A";
        int year = 2023;

        //when
        ResultActions resultActions = mockMvc.perform(get("/student/class="+className+"&year="+year)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test get all students from class with ADMIN role return 200 HTTP status and students in response body")
    @WithMockUser(authorities = {"ADMIN","TEACHER"})
    void getAllStudentsFromClass_givenAdminOrTeacherRole_shouldReturn_200_HTTPStatus_andStudentsInResponseBody() throws Exception {

        //given
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

    @Test
    @DisplayName("Test change student class with roles without permissions return 403 HTTP status")
    @WithMockUser(authorities = {"PARENT","STUDENT"})
    void changeStudentClass_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {

        //given
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
                        .param("className",newClassName)
                        .param("classYear",String.valueOf(newClassYear))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test change student class with ADMIN or TEACHER role return 204 HTTP status and changed student class")
    @WithMockUser(authorities = {"ADMIN","TEACHER"})
    void changeStudentClass_givenAdminOrTeacherRole_shouldReturn_204_HTTPStatus_andChangedStudentClass() throws Exception {

        //given
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
