package com.github.starwacki.components.student.service;

import com.github.starwacki.common.model.account.Parent;
import com.github.starwacki.common.model.account.Role;
import com.github.starwacki.common.model.account.Student;
import com.github.starwacki.common.model.account.Teacher;
import com.github.starwacki.common.model.grades.Degree;
import com.github.starwacki.common.repositories.GradeRepository;
import com.github.starwacki.common.repositories.StudentRepository;
import com.github.starwacki.common.repositories.TeacherRepository;
import com.github.starwacki.components.student.dto.GradeDTO;
import com.github.starwacki.components.student.dto.GradeViewDTO;
import com.github.starwacki.components.student.dto.StudentGradesDTO;
import com.github.starwacki.components.student.dto.SubjectDTO;
import com.github.starwacki.components.student.exceptions.StudentNotFoundException;
import com.github.starwacki.components.student.exceptions.SubjectNotFoundException;
import com.github.starwacki.components.student.exceptions.TeacherAccountNotFoundException;
import com.github.starwacki.common.model.grades.Grade;
import com.github.starwacki.common.model.school_class.SchoolClass;
import com.github.starwacki.common.model.grades.Subject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentGradeServiceUnitTest {

    @InjectMocks
    private StudentGradeService studentGradeService;
    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;

    private static Student getStudentTestAccount() {
        return Student.builder()
                .firstname("firstname")
                .lastname("lastname")
                .schoolClass(new SchoolClass("2A",2023))
                .parent(Parent.builder().phoneNumber("111222333").build())
                .username("username")
                .password("password")
                .role(Role.STUDENT)
                .build();
    }

    private static Grade getGradeTest(int weight, Degree degree) {
        return Grade.builder()
                .weight(weight)
                .id(0)
                .degree(degree)
                .addedBy(Teacher.builder()
                        .firstname("TeacherOne")
                        .lastname("Teacher")
                        .build())
                .addedDate(LocalDate.of(2022,10,10))
                .subject(Subject.PHYSICS)
                .description("description")
                .build();
    }

    private static GradeDTO getGradeDto(int addingTeacherId) {
        return GradeDTO.builder()
                .subject(Subject.PHYSICS)
                .degree(Degree.FIVE.getSymbol())
                .description("Klasówka")
                .weight(4)
                .addingTeacherId(addingTeacherId)
                .build();
    }

    @Test
    @DisplayName("Test get subject grades when student no exist throw exception")
    void getOneSubjectGrades_givenNoExistStudent_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        int subjectId = 1;
        given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        //then
        assertThrows(StudentNotFoundException.class,() -> studentGradeService.getOneSubjectGrades(studentId,subjectId));
    }

    @Test
    @DisplayName("Test get subject grades when student no exist throw exception with message ")
    void getOneSubjectGrades_givenNoExistStudent_shouldThrowStudentNotFoundExceptionWithExceptionMessage() {
        //given
        int studentId = 0;
        int subjectId = 1;
        given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(StudentNotFoundException.class,() -> studentGradeService.getOneSubjectGrades(studentId,subjectId));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Student not found id: " + studentId;
        assertEquals(expectedMessage,actualMessage);
    }

    @Test
    @DisplayName("Test get subject grades when subject no exist throw exception")
    void getOneSubjectGrades_givenNoExistSubjectID_shouldThrowSubjectNotFoundException() {
        //given
        int studentId = 0;
        int subjectId = 1111;
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));

        //then
        assertThrows(SubjectNotFoundException.class,() -> studentGradeService.getOneSubjectGrades(studentId,subjectId));
    }

    @Test
    @DisplayName("Test get subject grades when subject no exist throw exception with message ")
    void getOneSubjectGrades_givenNoExistSubjectID_shouldThrowSubjectNotFoundExceptionWithExceptionMessage() {
        //given
        int studentId = 0;
        int subjectId = 111;
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));

        //when
        Exception exception = assertThrows(SubjectNotFoundException.class,() -> studentGradeService.getOneSubjectGrades(studentId,subjectId));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Subject not found, id: " + subjectId;
        assertEquals(expectedMessage,actualMessage);
    }

    @Test
    @DisplayName("Test get subject grades when subject and student id no exist throw exception ")
    void getOneSubjectGrades_givenNoExistStudentAndSubjectID_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        int subjectId = 111;
        given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        //then
        assertThrows(StudentNotFoundException.class,() -> studentGradeService.getOneSubjectGrades(studentId,subjectId));
    }

    @Test
    @DisplayName("Test get subject grades when student not have subject grades")
    void getOneSubjectGrades_givenStudentWithoutGrades_shouldReturnEmptyListOfGrades() {
        //given
        int studentId = 0;
        int subjectId = Subject.PHYSICS.ordinal();
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(gradeRepository.findAllByStudentIdAndSubject(studentId,Subject.PHYSICS)).willReturn(List.of());

        //when
        StudentGradesDTO studentGrades = studentGradeService.getOneSubjectGrades(studentId,subjectId);

        //then
        int expectedSizeOfGrades = 0;
        assertThat(studentGrades.subjectGrades().get(0).grades(),hasSize(expectedSizeOfGrades));
    }

    @Test
    @DisplayName("Test get subject grades when student not have subject grades return correct subject weighted average")
    void getOneSubjectGrades_givenStudentWithoutGrades_shouldReturnSubjectWithZeroWeightedAverage() {
        //given
        int studentId = 0;
        int subjectId = Subject.PHYSICS.ordinal();
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(gradeRepository.findAllByStudentIdAndSubject(studentId,Subject.PHYSICS)).willReturn(List.of());

        //when
        StudentGradesDTO studentGrades = studentGradeService.getOneSubjectGrades(studentId,subjectId);

        //then
        String expectedAverage = "0,00";
        assertThat(studentGrades.subjectGrades().get(0).gradeAverage(),is(equalTo(expectedAverage)));
    }

    @Test
    @DisplayName("Test get subject grades when student have grades")
    void getOneSubjectGrades_givenStudentWith4Grades_shouldReturnListOf4Grades() {
        //give
        int studentId = 0;
        int subjectId = Subject.PHYSICS.ordinal();
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(gradeRepository.findAllByStudentIdAndSubject(studentId,Subject.PHYSICS)).willReturn(
                List.of(
                        getGradeTest(1,Degree.FIVE),
                        getGradeTest(1,Degree.FOUR),
                        getGradeTest(1,Degree.THREE),
                        getGradeTest(2,Degree.NB)
        ));

        //when
        StudentGradesDTO studentGrades = studentGradeService.getOneSubjectGrades(studentId,subjectId);

        //then
        int expectedSizeOfGrades = 4;
        assertThat(studentGrades.subjectGrades().get(0).grades(),hasSize(expectedSizeOfGrades));
    }

    @Test
    @DisplayName("Test get subject grades when student have grades return correct subject weighted average")
    void getOneSubjectGrades_givenStudentWith3GradesWithDifferentWeights_shouldReturn_2_63_weightedAverage() {
        //given
        int studentId = 0;
        int subjectId = Subject.PHYSICS.ordinal();
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(gradeRepository.findAllByStudentIdAndSubject(studentId,Subject.PHYSICS)).willReturn(List.of(
                getGradeTest(3,Degree.FIVE),getGradeTest(5,Degree.ONE),getGradeTest(3,Degree.THREE)
        ));

        //when
        StudentGradesDTO studentGrades = studentGradeService.getOneSubjectGrades(studentId,subjectId);

        //then
        String expectedAverage = "2,64";
        assertThat(studentGrades.subjectGrades().get(0).gradeAverage(),is(equalTo(expectedAverage)));
    }

    @Test
    @DisplayName("Test get subject grades when student have grades return correct subject weighted average")
    void getOneSubjectGrades_givenStudentWith3GradesWithValuesAnd3WithoutValue_shouldReturn_2_63_weightedAverage() {
        //given
        int studentId = 0;
        int subjectId = Subject.PHYSICS.ordinal();
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(gradeRepository.findAllByStudentIdAndSubject(studentId,Subject.PHYSICS)).willReturn(List.of(
                getGradeTest(3,Degree.FIVE),getGradeTest(5,Degree.ONE),getGradeTest(3,Degree.THREE),
                getGradeTest(1,Degree.NB),getGradeTest(2,Degree.PLUS),getGradeTest(5, Degree.MINUS)
        ));

        //when
        StudentGradesDTO studentGrades = studentGradeService.getOneSubjectGrades(studentId,subjectId);

        //then
        String expectedAverage = "2,64";
        assertThat(studentGrades.subjectGrades().get(0).gradeAverage(),is(equalTo(expectedAverage)));
    }

    @Test
    @DisplayName("Test get all students subject grades when student no exist")
    void getAllSubjectGradesByStudentID_givenNoExistStudent_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        //then
        assertThrows(StudentNotFoundException.class,() -> studentGradeService.getAllSubjectGradesByStudentID(studentId));
    }

    @Test
    @DisplayName("Test get all students subject grades when student no have grades")
    void getAllSubjectGradesByStudentID_givenStudentWithNoHaveGrades_shouldReturn13ListsOfGrades() {
        //given
        int studentId = 2;
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        for(Subject subject : Subject.values()) {
            given(gradeRepository.findAllByStudentIdAndSubject(studentId,subject)).willReturn(List.of());
        }

        //when
        StudentGradesDTO studentGrades = studentGradeService.getAllSubjectGradesByStudentID(studentId);


        //then
        int expectedSubjectsSize = 13;
        assertThat(studentGrades.subjectGrades(),hasSize(expectedSubjectsSize));
    }

    @Test
    @DisplayName("Test get all students subject grades when every subject have 3 grades")
    void getAllSubjectGradesByStudentID_givenStudent_whenEverySubjectHave3Grades_shouldReturn13ListsOfGradesWith3Grades() {
        //given
        int studentId = 2;
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        for(Subject subject : Subject.values()) {
            given(gradeRepository.findAllByStudentIdAndSubject(studentId,subject)).willReturn(
                    List.of(
                            getGradeTest(3,Degree.FIVE),
                            getGradeTest(5,Degree.ONE),
                            getGradeTest(3,Degree.THREE)
            ));
        }

        //when
        StudentGradesDTO studentGrades = studentGradeService.getAllSubjectGradesByStudentID(studentId);

        //then
        int gradesSize = 3;
        for(SubjectDTO subjectGrades : studentGrades.subjectGrades()) {
            assertThat(subjectGrades.grades(),hasSize(gradesSize));
        }
    }

    @Test
    @DisplayName("Test get all students subject grades when every subject have 3 grades return correct subject average")
    void getAllSubjectGradesByStudentID_givenStudent_whenEverySubjectHave3Grades_shouldReturn13ListsOfGradesWith_2_64_average() {
        //given
        int studentId = 0;
        Student student = getStudentTestAccount();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        for(Subject subject : Subject.values()) {
            given(gradeRepository.findAllByStudentIdAndSubject(studentId,subject)).willReturn(
                    List.of(
                            getGradeTest(3,Degree.FIVE),
                            getGradeTest(5,Degree.ONE),
                            getGradeTest(3,Degree.THREE)
                    ));
        }

        //when
        StudentGradesDTO studentGrades = studentGradeService.getAllSubjectGradesByStudentID(studentId);

        //then
        String expectedAverage = "2,64";
        for(SubjectDTO subjectGrades : studentGrades.subjectGrades()) {
            assertThat(subjectGrades.gradeAverage(), is(equalTo(expectedAverage)));
        }
    }


    @Test
    @DisplayName("Test get one grade by no exist student id and grade id throw exception")
    void getOneGrade_givenNoExistStudentIdOrGradeId_shouldThrowNewStudentNotFoundException() {
        //given
        int studentId = 0;
        int gradeId = 1;
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.empty());

        //then
        assertThrows(StudentNotFoundException.class,() -> studentGradeService.getOneGrade(studentId,gradeId));

    }

    @Test
    @DisplayName("Test get one grade by no exist student id and grade id throw exception with message")
    void getOneGrade_givenNoExistStudentIdOrGradeId_shouldThrowNewStudentNotFoundExceptionWithMessage() {
        //given
        int studentId = 0;
        int gradeId = 1;
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(StudentNotFoundException.class,() -> studentGradeService.getOneGrade(studentId,gradeId));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Student not found id: " + studentId + " with grade id: " + gradeId;
        assertThat(expectedMessage,is(equalTo(actualMessage)));
    }

    @Test
    @DisplayName("Test get one grade by student id and grade id return mapped gradeViewDTO with correct fields")
    void getOneGrade_givenStudentIdAndGradeId_shouldReturnGradeViewDtoWithCorrectFields() {
        //given
        int studentId = 0;
        int gradeId = 1;
        Grade grade = getGradeTest(1,Degree.FIVE);
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.of(grade));

        //when
        GradeViewDTO actualGradeDto = studentGradeService.getOneGrade(studentId,gradeId);

        //then
        assertEquals(grade.getWeight(),actualGradeDto.weight());
        assertEquals(grade.getDegree().getSymbol(),actualGradeDto.degree());
        assertEquals(grade.getDescription(),actualGradeDto.description());
        assertEquals(grade.getAddedDate(),actualGradeDto.addedDate());
    }

    @Test
    @DisplayName("Test get one grade by student id and grade id return mapped gradeViewDTO with added by firstname and lastname teacher")
    void getOneGrade_givenStudentIdAndGradeId_shouldReturnGradeViewDtoWithFirstnameAndLastnameOfTeacherInAddedBy() {
        //given
        int studentId = 0;
        int gradeId = 1;
        Grade grade = getGradeTest(1,Degree.FIVE);
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.of(grade));

        //when
        GradeViewDTO actualGradeDto = studentGradeService.getOneGrade(studentId,gradeId);
        String actualAddedBy = actualGradeDto.addedBy();

        //then
        String expectedAddedBy = grade.getAddedBy().getFirstname() + " " + grade.getAddedBy().getLastname();
        assertEquals(expectedAddedBy,actualAddedBy);
    }

    @Test
    @DisplayName("Test add grade to student when student no exist throw student not found exception")
    void  addGradeToStudent_givenGradeDTO_andNoExistStudentId_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        int addingTeacherId = 0;
        GradeDTO gradeDTO = getGradeDto(addingTeacherId);
        given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        //then
        assertThrows(StudentNotFoundException.class,() -> studentGradeService.addGradeToStudent(gradeDTO,studentId));
    }

    @Test
    @DisplayName("Test add grade to student when teacher no exist throw teacher not found exception")
    void  addGradeToStudent_givenGradeDTO_andNoExistTeacherId_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        int addingTeacherId = 0;
        GradeDTO gradeDTO = getGradeDto(addingTeacherId);
        given(studentRepository.findById(studentId)).willReturn(Optional.of(mock(Student.class)));
        given(teacherRepository.findById(addingTeacherId)).willReturn(Optional.empty());

        //then
        assertThrows(TeacherAccountNotFoundException.class,() -> studentGradeService.addGradeToStudent(gradeDTO,studentId));
    }

    @Test
    @DisplayName("Test add grade to student save grade to database")
    void  addGradeToStudent_givenGradeDTO_shouldSaveGradeToDatabase() {
        //given
        int studentId = 0;
        int addingTeacherId = 0;
        GradeDTO gradeDTO = getGradeDto(addingTeacherId);
        Student student = getStudentTestAccount();
        Teacher teacher = Teacher
                .builder()
                .firstname("Jan")
                .lastname("Szopa")
                .build();
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(teacherRepository.findById(addingTeacherId)).willReturn(Optional.of(teacher));

        //when
        studentGradeService.addGradeToStudent(gradeDTO,studentId);
        Grade expectedGrade = Grade
                .builder()
                .addedBy(teacher)
                .degree(Degree.FIVE)
                .weight(4)
                .description("Klasówka")
                .addedDate(LocalDate.now())
                .subject(Subject.PHYSICS)
                .student(student)
                .build();

        verify(gradeRepository).save(expectedGrade);
    }

    @Test
    @DisplayName("Test update grade when student or grade no exist throw exception")
    void updateGrade_givenNoExistStudentId_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        int addingTeacherId = 0;
        int gradeId = 0;
        GradeDTO gradeDTO = getGradeDto(addingTeacherId);
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.empty());

        //then
        assertThrows(StudentNotFoundException.class,() -> studentGradeService.updateGrade(studentId,gradeId,gradeDTO));
    }

    @Test
    @DisplayName("Test update grade save grade to database")
    void updateGrade_givenStudent_andTeacherId_andGradeDTO_shouldSaveDtoToDatabase() {
        //given
        int studentId = 0;
        int addingTeacherId = 0;
        int gradeId = 0;

        Student student = getStudentTestAccount();
        Teacher teacher = Teacher
                .builder()
                .firstname("Jan")
                .lastname("Szopa")
                .build();
        GradeDTO gradeDTO = getGradeDto(addingTeacherId);
        Grade expectedGrade = getGradeTest(2,Degree.FIVE);
        System.out.println(gradeDTO);
        expectedGrade.setStudent(student);
        expectedGrade.setAddedBy(teacher);
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.of(expectedGrade));
        given(gradeRepository.save(expectedGrade)).willReturn(expectedGrade);

        //when
        studentGradeService.updateGrade(studentId,gradeId,gradeDTO);

        //then
        verify(gradeRepository).save(expectedGrade);
    }

    @Test
    @DisplayName("Test update grade change grade description, weight and degree")
    void updateGrade_givenStudent_andTeacherId_andGradeDTO_shouldChangeGradeDescription_andWeight_andDegree() {
        //given
        int studentId = 0;
        int addingTeacherId = 0;
        int gradeId = 0;

        Student student = getStudentTestAccount();
        Teacher teacher = Teacher
                .builder()
                .firstname("Jan")
                .lastname("Szopa")
                .build();

        int newWeight = 10;
        String newDescription = "new description";
        String newDegree = "6";
        GradeDTO expectedUpdateGrade = GradeDTO.builder()
                .weight(10)
                .description(newDescription)
                .addingTeacherId(addingTeacherId)
                .weight(newWeight)
                .subject(Subject.PHYSICS)
                .degree(newDegree)
                .build();

        Grade expectedGrade = getGradeTest(2,Degree.FIVE);
        expectedGrade.setStudent(student);
        expectedGrade.setAddedBy(teacher);
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.of(expectedGrade));
        given(gradeRepository.save(expectedGrade)).willReturn(expectedGrade);

        //when
        GradeDTO actualGrade = studentGradeService.updateGrade(studentId,gradeId,expectedUpdateGrade);

        //then
        assertEquals(expectedUpdateGrade,actualGrade);
    }

    @Test
    @DisplayName("Test update grade not change grade subject and addingTeacherId")
    void updateGrade_givenStudent_andTeacherId_andGradeDTO_shouldNotChangeSubjectAndTeacherIDInUpdatedGrade() {
        //given
        int studentId = 0;
        int addingTeacherId = 0;
        int gradeId = 0;

        Student student = getStudentTestAccount();
        Teacher teacher = Teacher
                .builder()
                .firstname("Jan")
                .lastname("Szopa")
                .build();

        int newWeight = 10;
        String newDescription = "new description";
        String newDegree = "6";
        GradeDTO expectedUpdateGrade = GradeDTO.builder()
                .weight(10)
                .description(newDescription)
                .addingTeacherId(3)
                .weight(newWeight)
                .subject(Subject.MATHS)
                .degree(newDegree)
                .build();

        Grade expectedGrade = getGradeTest(2,Degree.FIVE);
        expectedGrade.setStudent(student);
        expectedGrade.setAddedBy(teacher);
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.of(expectedGrade));
        given(gradeRepository.save(expectedGrade)).willReturn(expectedGrade);

        //when
        GradeDTO actualGrade = studentGradeService.updateGrade(studentId,gradeId,expectedUpdateGrade);

        //then
        assertNotEquals(expectedUpdateGrade,actualGrade);
        assertEquals(addingTeacherId,actualGrade.addingTeacherId());
        assertEquals(Subject.PHYSICS,actualGrade.subject());
    }

    @Test
    @DisplayName("Test delete grade when student or grade no exist throw exception")
    void deleteStudentGrade_givenNoExistStudentIdOrGradeId_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        int gradeId = 0;
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.empty());

        //then
        assertThrows(StudentNotFoundException.class,() -> studentGradeService.deleteStudentGrade(studentId,gradeId));
    }

    @Test
    @DisplayName("Test delete grade delete grade from database")
    void deleteStudentGrade_givenStudentId_andGradeId_shouldDeleteGradeFromDatabase() {
        //given
        int studentId = 0;
        int gradeId = 0;
        Grade grade = getGradeTest(2,Degree.FIVE);
        given(gradeRepository.findByStudentIdAndId(studentId,gradeId)).willReturn(Optional.of(grade));

        //when
        studentGradeService.deleteStudentGrade(studentId,gradeId);

        //then
        verify(gradeRepository).delete(grade);
    }




}