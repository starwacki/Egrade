package com.github.starwacki.components.student;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountStudentControllerIntegrationTest {

//    @Autowired
//    private StudentRepository studentRepository;
//    @Autowired
//    private SchoolClassRepository schoolClassRepository;
//    @Autowired
//    private TeacherRepository teacherRepository;
//    @Autowired
//    private GradeRepository gradeRepository;
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @BeforeEach
//    public void prepareData() {
//        AccountStudent accountStudent = AccountStudent
//                .builder()
//                .username("username1")
//                .firstname("firstname1")
//                .lastname("lastname1")
//                .password("password1")
//                .role(Role.STUDENT)
//                .build();
//        Parent parent  = Parent
//                .builder()
//                .phoneNumber("111222333")
//                .build();
//        accountStudent.setAccountParent(parent);
//        String className = "2A";
//        int year = 2023;
//        SchoolClass schoolClass = new SchoolClass(className,year);
//        accountStudent.setSchoolClass(schoolClass);
//        studentRepository.save(accountStudent);
//    }
//
//    @Test
//    @DisplayName("Test get all students from class with roles without permissions return 403 HTTP status")
//    @WithMockUser(authorities = {"PARENT","STUDENT"})
//    void getAllStudentsFromClass_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {
//
//        //given
//        String className = "2A";
//        int year = 2023;
//
//        //when
//        ResultActions resultActions = mockMvc.perform(get("/student/class="+className+"&year="+year)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
//    }
//
//    @Test
//    @DisplayName("Test get all students from class with ADMIN role return 200 HTTP status and students in response body")
//    @WithMockUser(authorities = {"ADMIN","TEACHER"})
//    void getAllStudentsFromClass_givenAdminOrTeachdrRole_shouldReturn_200_HTTPStatus_andStudentsInResponseBody() throws Exception {
//
//        //given
//        String className = "2A";
//        int year = 2023;
//        int savedId = studentRepository.findByUsername("username1").get().getId();
//
//        //when
//        ResultActions resultActions = mockMvc.perform(get("/student/class="+className+"&year="+year)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        List<StudentDTO> expectedStudentsListInResponseBody = List.of(
//                StudentDTO
//                        .builder()
//                        .id(savedId)
//                        .className(className)
//                        .parentPhone("111222333")
//                        .year(year)
//                        .firstname("firstname1")
//                        .lastname("lastname1")
//                        .build()
//        );
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
//                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
//                        is(equalTo(objectMapper.writeValueAsString(expectedStudentsListInResponseBody)))));
//    }
//
//    @Test
//    @DisplayName("Test change student class with roles without permissions return 403 HTTP status")
//    @WithMockUser(authorities = {"PARENT","STUDENT"})
//    void changeStudentClass_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {
//
//        //given
//        String newClassName = "1A";
//        int newYear = 2023;
//        schoolClassRepository.save(new SchoolClass(newClassName,newYear));
//
//        int savedId = studentRepository.findByUsername("username1").get().getId();
//
//        //when
//        ResultActions resultActions = mockMvc.perform(put("/student/id="+savedId+"/class")
//                        .param("className",newClassName)
//                        .param("classYear",String.valueOf(newYear))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
//    }
//
//    @Test
//    @DisplayName("Test change student class with ADMIN or TEACHER role return 204 HTTP status and changed student class")
//    @WithMockUser(authorities = {"ADMIN","TEACHER"})
//    void changeStudentClass_givenAdminOrTeacherRole_shouldReturn_204_HTTPStatus_andChangedStudentClass() throws Exception {
//
//        //given
//        String newClassName = "1A";
//        int newYear = 2023;
//        SchoolClass newSchoolClass = new SchoolClass(newClassName,newYear);
//        schoolClassRepository.save(newSchoolClass);
//
//        int savedId = studentRepository.findByUsername("username1").get().getId();
//
//        //when
//        ResultActions resultActions = mockMvc.perform(put("/student/id="+savedId+"/class")
//                .param("className",newClassName)
//                .param("classYear",String.valueOf(newYear))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()));
//        assertThat(studentRepository.findById(savedId).get().getSchoolClass(),
//                is(equalTo(newSchoolClass)));
//    }
//




}
