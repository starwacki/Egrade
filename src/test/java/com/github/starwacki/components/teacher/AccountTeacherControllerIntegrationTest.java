package com.github.starwacki.components.teacher;

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
class AccountTeacherControllerIntegrationTest {

//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private TeacherRepository teacherRepository;
//    @Autowired
//    private SchoolClassRepository schoolClassRepository;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void prepareData() {
//        AccountTeacher accountTeacher = AccountTeacher
//                .builder()
//                .firstname("firstname")
//                .lastname("lastname")
//                .role(Role.TEACHER)
//                .email("email@wp.pl")
//                .password("password")
//                .workPhone("111222333")
//                .subject(Subject.PHYSICS)
//                .build();
//        SchoolClass schoolClass1 = new SchoolClass("1A",2023);
//        schoolClassRepository.save(schoolClass1);
//        accountTeacher.setClasses(Set.of(schoolClass1));
//        teacherRepository.save(accountTeacher);
//    }
//
//
//    @Test
//    @DisplayName("Test get teacher classes return 200 HTTP status and teacher classes in responseBody")
//    @WithMockUser(authorities = {"ADMIN","TEACHER","STUDENT","PARENT"})
//    void  getTeacherClasses_givenTeacherId_shouldReturn_200_HTTPStatus_andTeacherClassesInResponseBody() throws Exception {
//        //given
//        int teacherId = teacherRepository.findAll().get(0).getId();
//        System.out.println(teacherRepository.findAll());
//
//        //when
//        ResultActions resultActions  = mockMvc.perform(get("/teacher/id="+teacherId+"/classes")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        String expectedResponseBody = objectMapper.writeValueAsString(
//               Set.of(SchoolClassDTO
//                       .builder()
//                       .className("1A")
//                       .year(2023)
//                       .build()));
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
//                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
//                        is(equalTo(expectedResponseBody))));
//
//    }
//
//    @Test
//    @DisplayName("Test add school class to teacher with roles without permissions return 403 HTTP status")
//    @WithMockUser(authorities = {"TEACHER","STUDENT","PARENT"})
//    void  addSchoolClassToTeacher_givenRolesWithoutPermissions_shouldReturn_403_HTTPStatus() throws Exception {
//        //given
//        int teacherId = teacherRepository.findAll().get(0).getId();
//        SchoolClassDTO schoolClassDTO = SchoolClassDTO.builder()
//                .year(2025)
//                .className("3A").build();
//        SchoolClass schoolClass = new SchoolClass("3A",2025);
//        schoolClassRepository.save(schoolClass);
//
//        //when
//        ResultActions resultActions  = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
//                        .content(objectMapper.writeValueAsString(schoolClassDTO))
//                        .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
//
//    }
//
//    @Test
//    @DisplayName("Test add school class to teacher with ADMIN role return 204 HTTP status and add class to teacher")
//    @WithMockUser(authorities = "ADMIN")
//    void  addSchoolClassToTeacher_givenAdminRole_shouldReturn_204_HTTPStatus_andAddClassToTeacher() throws Exception {
//        //given
//        int teacherId = teacherRepository.findAll().get(0).getId();
//        SchoolClassDTO schoolClassDTO = SchoolClassDTO.builder()
//                .year(2025)
//                .className("3A").build();
//        SchoolClass schoolClass = new SchoolClass("3A",2025);
//        schoolClassRepository.save(schoolClass);
//
//        //when
//        ResultActions resultActions  = mockMvc.perform(put("/teacher/id="+teacherId+"/classes")
//                .content(objectMapper.writeValueAsString(schoolClassDTO))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()));
//        Set<SchoolClass> actualTeacherClasses = teacherRepository.findTeacherById(teacherId).getClasses();
//        assertTrue(actualTeacherClasses.contains(schoolClass));
//    }
//
//    @Test
//    @DisplayName("Test get all teachers information with any role return 200 HTTP status informations in response body")
//    @WithMockUser(authorities = {"STUDENT","PARENT","TEACHER","ADMIN"})
//    void   getAllTeachersInformation_shouldReturn_200_HTTPStatus_InformationAboutTeacher() throws Exception {
//
//        //when
//        ResultActions resultActions  = mockMvc.perform(get("/teacher/teachers")
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        List<TeacherDTO> expectedTeachersDTO = List.of(
//                TeacherDTO
//                        .builder()
//                        .firstname("firstname")
//                        .lastname("lastname")
//                        .subject(Subject.PHYSICS.toString())
//                        .email("email@wp.pl")
//                        .phone("111222333")
//                        .build()
//        );
//
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
//                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
//                        is(equalTo(objectMapper.writeValueAsString(expectedTeachersDTO)))));
//    }
//

}
