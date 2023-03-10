package com.github.starwacki.components.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.global.model.account.Parent;
import com.github.starwacki.global.model.account.Role;
import com.github.starwacki.global.model.account.Student;
import com.github.starwacki.global.model.account.Teacher;
import com.github.starwacki.global.model.grades.Subject;
import com.github.starwacki.global.model.school_class.SchoolClass;
import com.github.starwacki.global.repositories.ParentRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import com.github.starwacki.global.security.AES;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    @DisplayName("Test add student with roles without permissions return 403 HTTP status")
    @WithMockUser(roles = {"STUDENT","PARENT","TEACHER",})
    void addStudent_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {

        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .className("2A")
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test add student with ADMIN role return 201 HTTP status and should save student to database")
    @WithMockUser(roles = "ADMIN")
    void addStudent_givenAdminRole_shouldReturn_201_HTTPStatus_andCreateStudentAccountWithProperFields() throws Exception {

        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .className("2A")
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));

        List<Student> studentsInDataBase = studentRepository.findAll();
        assertThat(studentsInDataBase, hasSize(1));
        assertThat(studentsInDataBase.get(0), allOf(
                hasProperty("username", equalTo("Firstname.LastnameSTU1")),
                hasProperty("firstname", equalTo("Firstname")),
                hasProperty("lastname", equalTo("Lastname"))
        ));
    }

    @Test
    @DisplayName("Test add student with ADMIN role return 201 HTTP status and should save parent to database")
    @WithMockUser(roles = "ADMIN")
    void addStudent_givenAdminRole_shouldReturn_201_HTTPStatus_andCreateParentAccountWithProperFields() throws Exception {

        //given
        AccountStudentDTO accountStudentDTO = AccountStudentDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .className("2A")
                .year(2023)
                .parentPhoneNumber("111222333")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/account/student")
                .content(objectMapper.writeValueAsString(accountStudentDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));

        List<Parent> parentsInDatabase = parentRepository.findAll();
        assertThat(parentsInDatabase, hasSize(1));
        assertThat(parentsInDatabase.get(0), allOf(
                hasProperty("username", equalTo("Firstname.LastnameRO1")),
                hasProperty("firstname", equalTo("Firstname")),
                hasProperty("lastname", equalTo("Lastname")),
                hasProperty("phoneNumber",equalTo("111222333"))
        ));
    }

    @Test
    @DisplayName("Test add student from csv file with roles without permissions return 403 HTTP status")
    @WithMockUser(roles = {"STUDENT","PARENT","TEACHER",})
    void addStudentFromCSVFile_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {

        //given
        String filePath =  "src/test/resources/csv_generator_test/test_adding_student.csv";
        String[] data = {
                "Lukasz,Szwacz,2021,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        };

        new File(filePath).createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,false));
        writer.write("Imie,Nazwisko,Rok,Nazwa klasy,Numer do rodzica" + "\n");
        for(String line : data) {
            writer.write(line + "\n");
        }
        writer.close();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/account/students")
                .param("pathname",filePath)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
        new File(filePath).delete();
    }

    @Test
    @DisplayName("Test add student from csv file with ADMIN role return 201 HTTP status and add students and parents to database")
    @WithMockUser(roles = "ADMIN")
    void addStudentFromCSVFile_givenAdminRole_shouldReturn_201_HTTPStatus_andAddStudentsAndParentsToDatabase() throws Exception {

        //given
        String filePath =  "src/test/resources/csv_generator_test/test_adding_student.csv";
        String[] data = {
                "Lukasz,Szwacz,2021,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        };

        new File(filePath).createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,false));
        writer.write("Imie,Nazwisko,Rok,Nazwa klasy,Numer do rodzica" + "\n");
        for(String line : data) {
            writer.write(line + "\n");
        }
        writer.close();


        //when
        ResultActions resultActions  = mockMvc.perform(post("/account/students")
                .param("pathname",filePath)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        assertThat(studentRepository.findAll(),hasSize(4));
        assertThat(parentRepository.findAll(),hasSize(4));
        new File(filePath).delete();
    }

    @Test
    @DisplayName("Test add teacher with roles without permissions return 403 HTTP status")
    @WithMockUser(roles = {"STUDENT","PARENT","TEACHER",})
    void addTeacher_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {

        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .subject(Subject.PHYSICS)
                .email("email@wp.pl")
                .workPhone("111222333")
                .build();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/account/teacher")
                .content(objectMapper.writeValueAsString(accountTeacherDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test add teacher with ADMIN role return 201 HTTP status and should save student to database")
    @WithMockUser(roles = "ADMIN")
    void addTeacher_givenAdminRole_shouldReturn_201_HTTPStatus_andCreateTeacherAccountWithProperFields() throws Exception {

        //given
        AccountTeacherDTO accountTeacherDTO = AccountTeacherDTO
                .builder()
                .firstname("Firstname")
                .lastname("Lastname")
                .subject(Subject.PHYSICS)
                .email("email@wp.pl")
                .workPhone("111222333")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/account/teacher")
                .content(objectMapper.writeValueAsString(accountTeacherDTO))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));

        List<Teacher> teachersInDatabase = teacherRepository.findAll();
        assertThat(teachersInDatabase, hasSize(1));
        assertThat(teachersInDatabase.get(0), allOf(
                hasProperty("username", equalTo("Firstname.LastnameNAU1")),
                hasProperty("firstname", equalTo("Firstname")),
                hasProperty("lastname", equalTo("Lastname")),
                hasProperty("workPhone", equalTo("111222333")),
                hasProperty("email", equalTo("email@wp.pl")),
                hasProperty("subject", equalTo(Subject.PHYSICS))
        ));
    }

    @Test
    @DisplayName("Test get account by id with roles without permissions return 403 HTTP status")
    @WithMockUser(roles = {"STUDENT","PARENT","TEACHER"})
    void getAccountById_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {

        //given
        Student student = Student
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .role(Role.STUDENT)
                .schoolClass(new SchoolClass("1A",2023))
                .password("password")
                .build();
        studentRepository.save(student);
        int id = studentRepository.findAll().get(0).getId();
        Role role = Role.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(get("/account/"+role+"="+id)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test get account by id with ADMIN role return 201 HTTP status and should save student to database")
    @WithMockUser(roles = "ADMIN")
    void getAccountById_givenAdminRole_shouldReturn_201_HTTPStatus_andReturnStudentWithCorrectFields() throws Exception {

        //given
        Student student = Student
                .builder()
                .username("username")
                .firstname("firstname")
                .lastname("lastname")
                .role(Role.STUDENT)
                .schoolClass(new SchoolClass("1A",2023))
                .password(AES.encrypt("password"))
                .build();
        studentRepository.save(student);
        int id = studentRepository.findAll().get(0).getId();
        Role role = Role.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(get("/account/"+role+"="+id)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        AccountViewDTO expectedAccountViewDTO = AccountViewDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .accountType(Role.STUDENT.toString())
                .id(id)
                .username("username")
                .password("password")
                .build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(expectedAccountViewDTO)))));
    }

    @Test
    @DisplayName("Test delete account by id with roles without permissions return 403 HTTP status")
    @WithMockUser(roles = {"STUDENT","PARENT","TEACHER"})
    void deleteAccountById_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {

        //given
        Student student = Student
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .role(Role.STUDENT)
                .schoolClass(new SchoolClass("1A",2023))
                .password("password")
                .build();
        studentRepository.save(student);
        int id = studentRepository.findAll().get(0).getId();
        Role role = Role.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(delete("/account/"+role+"="+id)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @DisplayName("Test delete account by id with ADMIN role return 201 HTTP status and should save student to database")
    @WithMockUser(roles = "ADMIN")
    void deleteAccountById_givenAdminRole_shouldReturn_201_HTTPStatus_andDeleteAccountFromDatabase() throws Exception {

        //given
        Student student = Student
                .builder()
                .username("username")
                .firstname("firstname")
                .lastname("lastname")
                .role(Role.STUDENT)
                .schoolClass(new SchoolClass("1A",2023))
                .password(AES.encrypt("password"))
                .build();
        studentRepository.save(student);
        int id = studentRepository.findAll().get(0).getId();
        Role role = Role.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(delete("/account/"+role+"="+id)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        AccountViewDTO expectedAccountViewDTO = AccountViewDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .accountType(Role.STUDENT.toString())
                .id(id)
                .username("username")
                .password("password")
                .build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(expectedAccountViewDTO)))));
        assertThat(studentRepository.findById(id),is(Optional.empty()));
    }

    @Test
    @DisplayName("Test change account password with any role return 200 HTTP status and should change password")
    @WithMockUser(roles = "ADMIN")
    void changeAccountPassword_givenAdminRole_shouldReturn_200_HTTPStatus_andDeleteAccountFromDatabase() throws Exception {

        //given
        String passwordInDatabase = "password";
        String newPassword = "Password1.";
        Student student = Student
                .builder()
                .username("username")
                .firstname("firstname")
                .lastname("lastname")
                .role(Role.STUDENT)
                .schoolClass(new SchoolClass("1A",2023))
                .password(AES.encrypt(passwordInDatabase))
                .build();
        studentRepository.save(student);
        int id = studentRepository.findAll().get(0).getId();
        Role role = Role.STUDENT;

        //when
        ResultActions resultActions  = mockMvc.perform(put("/account/"+role+"="+id)
                .param("oldPassword",passwordInDatabase)
                .param("newPassword",newPassword)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        AccountViewDTO expectedAccountViewDTO = AccountViewDTO
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .accountType(Role.STUDENT.toString())
                .id(id)
                .username("username")
                .password(newPassword)
                .build();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(objectMapper.writeValueAsString(expectedAccountViewDTO)))));
    }













}
