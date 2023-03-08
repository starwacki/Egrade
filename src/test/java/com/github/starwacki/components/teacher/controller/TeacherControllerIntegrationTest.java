package com.github.starwacki.components.teacher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.teacher.dto.SchoolClassDTO;
import com.github.starwacki.global.model.account.Role;
import com.github.starwacki.global.model.account.Teacher;
import com.github.starwacki.global.model.grades.Subject;
import com.github.starwacki.global.model.school_class.SchoolClass;
import com.github.starwacki.global.repositories.ParentRepository;
import com.github.starwacki.global.repositories.SchoolClassRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import org.junit.jupiter.api.*;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SchoolClassRepository schoolClassRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void prepareData() {
        Teacher teacher = Teacher
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .role(Role.TEACHER)
                .email("email@wp.pl")
                .password("password")
                .workPhone("111222333")
                .subject(Subject.PHYSICS)
                .build();
        SchoolClass schoolClass1 = new SchoolClass("1A",2023);
        schoolClassRepository.save(schoolClass1);
        teacher.setClasses(Set.of(schoolClass1));
        teacherRepository.save(teacher);
    }


    @Test
    @DisplayName("Test get teacher classes return 200 HTTP status and teacher classes in responseBody")
    @WithMockUser(roles = {"ADMIN","STUDENT","PARENT"})
    void  getTeacherClasses_givenTeacherId_shouldReturn_200_HTTPStatus_andTeacherClassesInResponseBody() throws Exception {
        //given
        int teacherId = teacherRepository.findAll().get(0).getId();
        System.out.println(teacherRepository.findAll());

        //when
        ResultActions resultActions  = mockMvc.perform(get("/teacher/id="+teacherId+"/classes")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(
               Set.of(SchoolClassDTO
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
    @WithMockUser(roles = {"TEACHER","STUDENT","PARENT"})
    void  addSchoolClassToTeacher_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {
        //given
        int teacherId = teacherRepository.findAll().get(0).getId();
        SchoolClassDTO schoolClassDTO = SchoolClassDTO.builder()
                .year(2025)
                .className("3A").build();
        SchoolClass schoolClass = new SchoolClass("3A",2025);
        schoolClassRepository.save(schoolClass);

        //when
        ResultActions resultActions  = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
                        .content(objectMapper.writeValueAsString(schoolClassDTO))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));

    }

    @Test
    @DisplayName("Test add school class to teacher with ADMIN role return 200 HTTP status and add class to teacher")
    @WithMockUser(roles = "ADMIN")
    void  addSchoolClassToTeacher_givenAdminRole_shouldReturn_200_HTTPStatus_andAddClassToTeacher() throws Exception {
        //given
        int teacherId = teacherRepository.findAll().get(0).getId();
        SchoolClassDTO schoolClassDTO = SchoolClassDTO.builder()
                .year(2025)
                .className("3A").build();
        SchoolClass schoolClass = new SchoolClass("3A",2025);
        schoolClassRepository.save(schoolClass);

        //when
        ResultActions resultActions  = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
                .content(objectMapper.writeValueAsString(schoolClassDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));

    }


}
