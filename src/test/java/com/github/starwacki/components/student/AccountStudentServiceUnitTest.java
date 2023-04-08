package com.github.starwacki.components.student;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
class AccountStudentServiceUnitTest {

//    @InjectMocks
//    private StudentService studentService;
//    @Mock
//    private StudentRepository studentRepository;
//    @Mock
//    private SchoolClassRepository schoolClassRepository;
//
//
//    private static AccountStudent getStudentTestAccount() {
//        return AccountStudent.builder()
//                .firstname("firstname")
//                .lastname("lastname")
//                .schoolClass(new SchoolClass("2A",2023))
//                .parent(Parent.builder().phoneNumber("111222333").build())
//                .username("username")
//                .password("password")
//                .role(Role.STUDENT)
//                .build();
//    }
//
//    @Test
//    @DisplayName("Test get students from class when class have students")
//    void  getAllStudentsFromClass_givenSchoolClassWith5Students_shouldReturnListOf5Size() {
//        //given
//        List<AccountStudent> studentsFromClasses = List.of(getStudentTestAccount(),getStudentTestAccount(),getStudentTestAccount()
//        ,getStudentTestAccount(),getStudentTestAccount());
//        SchoolClass schoolClass = new SchoolClass("2A",2023);
//        given(studentRepository.findAllBySchoolClassNameAndSchoolClassClassYear(
//                        schoolClass.getName(),schoolClass.getClassYear())).willReturn(studentsFromClasses);
//
//        //when
//        List<StudentDTO> expectedStudents = studentService.getAllStudentsFromClass(schoolClass.getName(),schoolClass.getClassYear());
//
//        //then
//        assertThat(expectedStudents,hasSize(5));
//    }
//
//    @Test
//    @DisplayName("Test get students from class when class haven't students")
//    void  getAllStudentsFromClass_givenEmptySchoolClass_shouldReturnEmptyList() {
//        //given
//        List<AccountStudent> studentsFromClasses = List.of();
//        SchoolClass schoolClass = new SchoolClass("2A",2023);
//        given(studentRepository.findAllBySchoolClassNameAndSchoolClassClassYear(
//                schoolClass.getName(),schoolClass.getClassYear())).willReturn(studentsFromClasses);
//
//        //when
//        List<StudentDTO> expectedStudents = studentService.getAllStudentsFromClass(schoolClass.getName(),schoolClass.getClassYear());
//
//        //then
//        assertThat(expectedStudents,is(empty()));
//    }
//
//    @Test
//    @DisplayName("Test get students from class return students with correct school class")
//    void  getAllStudentsFromClass_givenSchoolClassWith5Students_shouldReturnStudentDtoWithSameSchoolClassLikeSearchingClass() {
//        //given
//        List<AccountStudent> studentsFromClasses = List.of(getStudentTestAccount(),getStudentTestAccount(),getStudentTestAccount()
//                ,getStudentTestAccount(),getStudentTestAccount());
//        SchoolClass schoolClass = new SchoolClass("2A",2023);
//        given(studentRepository.findAllBySchoolClassNameAndSchoolClassClassYear(
//                schoolClass.getName(),schoolClass.getClassYear())).willReturn(studentsFromClasses);
//
//        //when
//        List<StudentDTO> expectedStudents = studentService.getAllStudentsFromClass(schoolClass.getName(),schoolClass.getClassYear());
//
//        //then
//        StudentDTO returnedStudent = expectedStudents.get(0);
//        assertEquals(schoolClass.getClassYear(),returnedStudent.year());
//        assertEquals(schoolClass.getName(),returnedStudent.className());
//    }
//
//    @Test
//    @DisplayName("Test get students from class return correct StudentDTO fields")
//    void  getAllStudentsFromClass_givenSchoolClassWithOneStudents_shouldReturnCorrectStudentDTO() {
//        //given
//        List<AccountStudent> studentsFromClasses = List.of(getStudentTestAccount());
//        SchoolClass schoolClass = new SchoolClass("2A",2023);
//        given(studentRepository.findAllBySchoolClassNameAndSchoolClassClassYear(
//                schoolClass.getName(),schoolClass.getClassYear())).willReturn(studentsFromClasses);
//
//        //when
//        List<StudentDTO> expectedStudents = studentService.getAllStudentsFromClass(schoolClass.getName(),schoolClass.getClassYear());
//
//        //then
//        StudentDTO returnedStudent = expectedStudents.get(0);
//        assertEquals("firstname", returnedStudent.firstname());
//        assertEquals("lastname", returnedStudent.lastname());
//        assertEquals(2023, returnedStudent.year());
//        assertEquals("2A",returnedStudent.className());
//        assertEquals("111222333",returnedStudent.parentPhone());
//    }
//
//    @Test
//    @DisplayName("Test change student class when student no exist throw exception")
//    void changeStudentClass_givenNoExistStudent_shouldThrowStudentNotFoundException() {
//        //given
//        int studentId = 1;
//        int classYear = 2023;
//        String className = "2A";
//        given(studentRepository.findById(1)).willReturn(Optional.empty());
//
//        //then
//        assertThrows(StudentNotFoundException.class,() -> studentService.changeStudentClass(studentId,className,classYear));
//    }
//
//    @Test
//    @DisplayName("Test change student class when student no exist throw exception with message")
//    void changeStudentClass_givenNoExistStudent_shouldThrowStudentNotFoundExceptionWithCorrectMessage() {
//        //given
//        int studentId = 1;
//        int classYear = 2023;
//        String className = "2A";
//        given(studentRepository.findById(1)).willReturn(Optional.empty());
//
//        //when
//        Exception exception = assertThrows(StudentNotFoundException.class,() ->
//                studentService.changeStudentClass(studentId,className,classYear));
//        String actualMessage = exception.getMessage();
//
//        //then
//        String expectedMessage = "Student not found id: " + studentId;
//        assertEquals(expectedMessage,actualMessage);
//    }
//
//
//


}