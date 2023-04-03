package com.github.starwacki.components.student.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.student.dto.GradeDTO;
import com.github.starwacki.components.student.dto.GradeViewDTO;
import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.common.model.account.Parent;
import com.github.starwacki.common.model.account.Role;
import com.github.starwacki.common.model.account.Student;
import com.github.starwacki.common.model.account.Teacher;
import com.github.starwacki.common.model.grades.Degree;
import com.github.starwacki.common.model.grades.Grade;
import com.github.starwacki.common.model.grades.Subject;
import com.github.starwacki.common.model.school_class.SchoolClass;
import com.github.starwacki.common.repositories.GradeRepository;
import com.github.starwacki.common.repositories.SchoolClassRepository;
import com.github.starwacki.common.repositories.StudentRepository;
import com.github.starwacki.common.repositories.TeacherRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StudentControllerIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SchoolClassRepository schoolClassRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void prepareData() {
        Student student = Student
                .builder()
                .username("username1")
                .firstname("firstname1")
                .lastname("lastname1")
                .password("password1")
                .role(Role.STUDENT)
                .build();
        Parent parent  = Parent
                .builder()
                .phoneNumber("111222333")
                .build();
        student.setParent(parent);
        String className = "2A";
        int year = 2023;
        SchoolClass schoolClass = new SchoolClass(className,year);
        student.setSchoolClass(schoolClass);
        studentRepository.save(student);
    }

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
    void getAllStudentsFromClass_givenAdminOrTeachdrRole_shouldReturn_200_HTTPStatus_andStudentsInResponseBody() throws Exception {

        //given
        String className = "2A";
        int year = 2023;
        int savedId = studentRepository.findByUsername("username1").get().getId();

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
        int newYear = 2023;
        schoolClassRepository.save(new SchoolClass(newClassName,newYear));

        int savedId = studentRepository.findByUsername("username1").get().getId();

        //when
        ResultActions resultActions = mockMvc.perform(put("/student/id="+savedId+"/class")
                        .param("className",newClassName)
                        .param("classYear",String.valueOf(newYear))
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
        int newYear = 2023;
        SchoolClass newSchoolClass = new SchoolClass(newClassName,newYear);
        schoolClassRepository.save(newSchoolClass);

        int savedId = studentRepository.findByUsername("username1").get().getId();

        //when
        ResultActions resultActions = mockMvc.perform(put("/student/id="+savedId+"/class")
                .param("className",newClassName)
                .param("classYear",String.valueOf(newYear))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()));
        assertThat(studentRepository.findById(savedId).get().getSchoolClass(),
                is(equalTo(newSchoolClass)));
    }

    @Test
    @DisplayName("Test add grade to student class with roles without permissions return 403 HTTP status")
    @WithMockUser(authorities = {"PARENT","STUDENT"})
    void addGradeToStudent_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {

        //given
        int teacherID = teacherRepository.save(Teacher
                .builder()
                .firstname("teacherFirstname")
                .lastname("teacherLastname")
                .build()).getId();
        GradeDTO gradeDTO = GradeDTO
                .builder()
                .addingTeacherId(teacherID)
                .subject(Subject.PHYSICS)
                .weight(5)
                .description("Test from term test")
                .degree("6")
                .build();
        int savedId = studentRepository.findByUsername("username1").get().getId();

        //when
        ResultActions resultActions = mockMvc.perform(post("/student/id="+savedId+"/grade")
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
        int teacherID = teacherRepository.save(Teacher
                .builder()
                .firstname("teacherFirstname")
                .lastname("teacherLastname")
                .build()).getId();
        GradeDTO gradeDTO = GradeDTO
                .builder()
                .addingTeacherId(teacherID)
                .subject(Subject.PHYSICS)
                .weight(5)
                .description("Test from term test")
                .degree("6")
                .build();
        int savedId = studentRepository.findByUsername("username1").get().getId();

        //when
        ResultActions resultActions = mockMvc.perform(post("/student/id="+savedId+"/grade")
                .content(objectMapper.writeValueAsString(gradeDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        assertThat(gradeRepository.findAll(),hasSize(1));
        assertThat(gradeRepository.findAll().get(0),
                allOf(
                        hasProperty("description",equalTo("Test from term test")),
                        hasProperty("weight",equalTo(5)),
                        hasProperty("subject",equalTo(Subject.PHYSICS)),
                        hasProperty("degree",equalTo(Degree.SIX))
                ));
    }

    @Test
    @DisplayName("Test get student grade with any role return 200 HTTP status")
    @WithMockUser(authorities = {"PARENT","STUDENT","ADMIN","TEACHER"})
    void getStudentGrade_givenAnyRole_shouldReturn_200_HTTPStatus_andGradeViewDTOInResponseBody() throws Exception {

        //given
        Student student = (Student) studentRepository.findByUsername("username1").get();
        Teacher teacher = Teacher
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .build();
        teacherRepository.save(teacher);
        Grade grade = Grade
                .builder()
                .subject(Subject.PHYSICS)
                .student(student)
                .weight(5)
                .degree(Degree.SIX)
                .addedDate(LocalDate.of(2023,12,10))
                .description("Grade from term test")
                .addedBy(teacher)
                .build();
        int gradeId = gradeRepository.save(grade).getId();

        //when
        ResultActions resultActions = mockMvc.perform(get("/student/id="+student.getId()+"/grade="+gradeId)
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
        Student student = (Student) studentRepository.findByUsername("username1").get();
        Teacher teacher = Teacher
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .build();
        teacherRepository.save(teacher);
        Grade grade = Grade
                .builder()
                .subject(Subject.PHYSICS)
                .student(student)
                .weight(5)
                .degree(Degree.SIX)
                .addedDate(LocalDate.of(2023,12,10))
                .description("Grade from term test")
                .addedBy(teacher)
                .build();
        int gradeId = gradeRepository.save(grade).getId();

        //when
        ResultActions resultActions = mockMvc.perform(delete("/student/id="+student.getId()+"/grade="+gradeId)
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
        Student student = (Student) studentRepository.findByUsername("username1").get();
        Teacher teacher = Teacher
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .build();
        teacherRepository.save(teacher);
        Grade grade = Grade
                .builder()
                .subject(Subject.PHYSICS)
                .student(student)
                .weight(5)
                .degree(Degree.SIX)
                .addedDate(LocalDate.of(2023,12,10))
                .description("Grade from term test")
                .addedBy(teacher)
                .build();
        int gradeId = gradeRepository.save(grade).getId();

        //when
        ResultActions resultActions = mockMvc.perform(delete("/student/id="+student.getId()+"/grade="+gradeId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        assertThat(gradeRepository.findByStudentIdAndId(student.getId(),gradeId),
                is(equalTo(Optional.empty())));
    }

    @Test
    @DisplayName("Test get student grades with any role return 200 HTTP status and grades in response body")
    @WithMockUser(authorities = {"PARENT","STUDENT","ADMIN","TEACHER"})
    void getStudentGrades_givenAnyRole_shouldReturn_200_HTTPStatus_andReturnGradesInResponseBody() throws Exception {

        //given
        Student student = (Student) studentRepository.findByUsername("username1").get();
        Teacher teacher = Teacher
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .build();
        teacherRepository.save(teacher);
        Grade grade = Grade
                .builder()
                .subject(Subject.PHYSICS)
                .student(student)
                .weight(5)
                .degree(Degree.SIX)
                .addedDate(LocalDate.of(2023,12,10))
                .description("Grade from term test")
                .addedBy(teacher)
                .build();
        gradeRepository.save(grade);

        //when
        ResultActions resultActions = mockMvc.perform(get("/student/id="+student.getId()+"/grades")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        GradeViewDTO expectedGradeInResponseBody = GradeViewDTO
                .builder()
                .weight(5)
                .addedBy("firstname lastname")
                .addedDate(LocalDate.of(2023,12,10))
                .degree("6")
                .description("Grade from term test")
                .build();        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains(objectMapper.writeValueAsString(expectedGradeInResponseBody))));
    }

    @Test
    @DisplayName("Test get student subject grades with any role return 200 HTTP status and grades in response body")
    @WithMockUser(authorities = {"PARENT","STUDENT","ADMIN","TEACHER"})
    void  getStudentSubjectGrades_givenAnyRole_shouldReturn_200_HTTPStatus_andReturnGradesInResponseBody() throws Exception {

        //given
        Subject subject = Subject.PHYSICS;
        Student student = (Student) studentRepository.findByUsername("username1").get();
        Teacher teacher = Teacher
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .build();
        teacherRepository.save(teacher);
        Grade grade = Grade
                .builder()
                .subject(subject)
                .student(student)
                .weight(5)
                .degree(Degree.SIX)
                .addedDate(LocalDate.of(2023,12,10))
                .description("Grade from term test")
                .addedBy(teacher)
                .build();
        gradeRepository.save(grade);

        //when
        ResultActions resultActions = mockMvc.perform(get("/student/id="+student.getId()+"/grades/subject="+subject.ordinal())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        GradeViewDTO expectedGradeInResponseBody = GradeViewDTO
                .builder()
                .weight(5)
                .addedBy("firstname lastname")
                .addedDate(LocalDate.of(2023,12,10))
                .degree("6")
                .description("Grade from term test")
                .build();        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString()
                        .contains(objectMapper.writeValueAsString(expectedGradeInResponseBody))));
    }







}
