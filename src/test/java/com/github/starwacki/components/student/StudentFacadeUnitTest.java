package com.github.starwacki.components.student;


import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.components.student.exceptions.StudentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StudentFacadeUnitTest {

    @InjectMocks
    private StudentFacade studentFacade;
    @Mock
    private StudentRepository studentRepository;


    private static Student getStudentTestAccount() {
        return Student.builder()
                .firstname("firstname")
                .lastname("lastname")
                .schoolClassName("2A")
                .schoolClassYear(2023)
                .parentPhoneNumber("111222333")
                .build();
    }

    @Test
    @DisplayName("Test get students from class when class have students")
    void  getAllStudentsFromClass_givenSchoolClassWith5Students_shouldReturnListOf5Size() {

        //given
        String schoolClassName = "2A";
        int schoolClassYear = 2023;
        List<Student> studentsFromClasses = List.of(getStudentTestAccount(),getStudentTestAccount(),getStudentTestAccount()
        ,getStudentTestAccount(),getStudentTestAccount());
        given(studentRepository.findAllBySchoolClassNameAndSchoolClassYear(schoolClassName,schoolClassYear)).willReturn(studentsFromClasses);

        //when
        List<StudentDTO> expectedStudents = studentFacade.getAllStudentsFromClass(schoolClassName,schoolClassYear);

        //then
        assertThat(expectedStudents,hasSize(5));
    }

    @Test
    @DisplayName("Test get students from class when class haven't students")
    void  getAllStudentsFromClass_givenEmptySchoolClass_shouldReturnEmptyList() {

        //given
        String schoolClassName = "2A";
        int schoolClassYear = 2023;
        List<Student> studentsFromClasses = List.of();
        given(studentRepository.findAllBySchoolClassNameAndSchoolClassYear(schoolClassName,schoolClassYear)).willReturn(studentsFromClasses);

        //when
        List<StudentDTO> expectedStudents = studentFacade.getAllStudentsFromClass(schoolClassName,schoolClassYear);

        //then
        assertThat(expectedStudents,is(empty()));
    }

    @Test
    @DisplayName("Test get students from class return students with correct school class")
    void  getAllStudentsFromClass_givenSchoolClassWith5Students_shouldReturnStudentDtoWithSameSchoolClassLikeSearchingClass() {

        //given
        String schoolClassName = "2A";
        int schoolClassYear = 2023;
        List<Student> studentsFromClasses = List.of(getStudentTestAccount(),getStudentTestAccount(),getStudentTestAccount()
                ,getStudentTestAccount(),getStudentTestAccount());
        given(studentRepository.findAllBySchoolClassNameAndSchoolClassYear(schoolClassName,schoolClassYear)).willReturn(studentsFromClasses);

        //when
        List<StudentDTO> expectedStudents = studentFacade.getAllStudentsFromClass(schoolClassName,schoolClassYear);

        //then
        StudentDTO returnedStudent = expectedStudents.get(0);
        assertEquals(schoolClassYear,returnedStudent.year());
        assertEquals(schoolClassName,returnedStudent.className());
    }

    @Test
    @DisplayName("Test get students from class return correct StudentDTO fields")
    void  getAllStudentsFromClass_givenSchoolClassWithOneStudents_shouldReturnCorrectStudentDTO() {
        //given
        String schoolClassName = "2A";
        int schoolClassYear = 2023;
        List<Student> studentsFromClasses = List.of(getStudentTestAccount());
        given(studentRepository.findAllBySchoolClassNameAndSchoolClassYear(schoolClassName,schoolClassYear)).willReturn(studentsFromClasses);

        //when
        List<StudentDTO> expectedStudents = studentFacade.getAllStudentsFromClass(schoolClassName,schoolClassYear);

        //then
        StudentDTO returnedStudent = expectedStudents.get(0);
        assertEquals("firstname", returnedStudent.firstname());
        assertEquals("lastname", returnedStudent.lastname());
        assertEquals(2023, returnedStudent.year());
        assertEquals("2A",returnedStudent.className());
        assertEquals("111222333",returnedStudent.parentPhone());
    }

    @Test
    @DisplayName("Test change student class when student no exist throw exception")
    void changeStudentClass_givenNoExistStudent_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 1;
        int classYear = 2023;
        String className = "2A";
        given(studentRepository.findById(1)).willReturn(Optional.empty());

        //then
        assertThrows(StudentNotFoundException.class,() -> studentFacade.changeStudentClass(studentId,className,classYear));
    }

    @Test
    @DisplayName("Test change student class when student no exist throw exception with message")
    void changeStudentClass_givenNoExistStudent_shouldThrowStudentNotFoundExceptionWithCorrectMessage() {
        //given
        int studentId = 1;
        int classYear = 2023;
        String className = "2A";
        given(studentRepository.findById(1)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(StudentNotFoundException.class,() ->
                studentFacade.changeStudentClass(studentId,className,classYear));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Student not found id: " + studentId;
        assertEquals(expectedMessage,actualMessage);
    }





}