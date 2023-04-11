package com.github.starwacki.components.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.teacher.dto.TeacherResponseDTO;
import com.github.starwacki.components.teacher.dto.TeacherSchoolClassDTO;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ObjectMapper objectMapper;

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


    @Test
    @DisplayName("Test get teacher classes return 200 HTTP status and teacher classes in responseBody")
    @WithMockUser(authorities = {"ADMIN","TEACHER","STUDENT","PARENT"})
    void  getTeacherClasses_givenTeacherId_shouldReturn_200_HTTPStatus_andTeacherClassesInResponseBody() throws Exception {
        //given
        int teacherId = teacherRepository.findAll().get(0).getId();
        System.out.println(teacherRepository.findAll());

        //when
        ResultActions resultActions  = mockMvc.perform(get("/teacher/id="+teacherId+"/classes")
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

    @Test
    @DisplayName("Test add school class to teacher with roles without permissions return 403 HTTP status")
    @WithMockUser(authorities = {"TEACHER","STUDENT","PARENT"})
    void  addSchoolClassToTeacher_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {
        //given
        int teacherId = teacherRepository.findAll().get(0).getId();
        TeacherSchoolClassDTO schoolClassDTO = TeacherSchoolClassDTO.builder()
                .year(2025)
                .className("3A").build();

        //when
        ResultActions resultActions  = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
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
        int teacherId = teacherRepository.findAll().get(0).getId();
        TeacherSchoolClassDTO schoolClassDTO = TeacherSchoolClassDTO.builder()
                .year(2025)
                .className("3A").build();

        //when
        ResultActions resultActions  = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
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

    @Test
    @DisplayName("Test get all teachers information with any role return 200 HTTP status informations in response body")
    @WithMockUser(authorities = {"STUDENT","PARENT","TEACHER","ADMIN"})
    void   getAllTeachersInformation_shouldReturn_200_HTTPStatus_InformationAboutTeacher() throws Exception {

        //when
        ResultActions resultActions  = mockMvc.perform(get("/teacher/teachers")
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
