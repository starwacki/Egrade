package com.github.starwacki.components.grades;

import com.github.starwacki.components.grades.dto.GradeRequestDTO;
import com.github.starwacki.components.grades.dto.GradeResponeDTO;
import com.github.starwacki.components.grades.dto.SubjectResponseDTO;
import com.github.starwacki.components.grades.exceptions.GradeStudentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GradeFacadeUnitTest {

    @InjectMocks
    private GradeFacade gradeFacade;
    @Mock
    private GradeRepository gradeRepository;


    private static Grade getGradeTestWeightSymbol(int weight, GradeSymbolValue gradeSymbolValue) {
        return Grade.builder()
                .weight(weight)
                .id(0)
                .gradeSymbolValue(gradeSymbolValue)
                .addedBy("Szymon Kawka")
                .addedDate(LocalDate.of(2022,10,10))
                .gradeSubject(GradeSubject.PHYSICS)
                .description("description")
                .build();
    }

    private static Grade getGradeTestWeightSymbolSubject(int weight, GradeSymbolValue gradeSymbolValue,GradeSubject gradeSubject) {
        return Grade.builder()
                .weight(weight)
                .id(0)
                .gradeSymbolValue(gradeSymbolValue)
                .addedBy("Szymon Kawka")
                .addedDate(LocalDate.of(2022,10,10))
                .gradeSubject(gradeSubject)
                .description("description")
                .build();
    }

    private static GradeRequestDTO getGradeDto() {
        return GradeRequestDTO.builder()
                .subject(GradeSubject.PHYSICS.toString())
                .degree(GradeSymbolValue.FIVE.getSymbol())
                .description("Klasówka")
                .weight(4)
                .addedBy("Szymon Kawka")
                .studentID(1)
                .build();
    }


    @Test
    @DisplayName("Test get subject grades when student no exist throw exception")
    void getOneSubjectGrades_givenNoExistStudent_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        int subjectId = 1;
        GradeSubject gradeSubject = GradeSubject.values()[subjectId];
        given(gradeRepository.findAllByStudentIDAndGradeSubject(studentId,gradeSubject)).willReturn(Optional.empty());

        //then
        assertThrows(GradeStudentNotFoundException.class,() -> gradeFacade.getOneSubjectGrades(studentId,subjectId));
    }

    @Test
    @DisplayName("Test get subject grades when student no exist throw exception with message ")
    void getOneSubjectGrades_givenNoExistStudent_shouldThrowStudentNotFoundExceptionWithExceptionMessage() {
        //given
        int studentId = 0;
        int subjectId = 1;
        GradeSubject gradeSubject = GradeSubject.values()[subjectId];
        given(gradeRepository.findAllByStudentIDAndGradeSubject(studentId,gradeSubject)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(GradeStudentNotFoundException.class,() -> gradeFacade.getOneSubjectGrades(studentId,subjectId));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Student not found id: " + studentId;
        assertEquals(expectedMessage,actualMessage);
    }


    @Test
    @DisplayName("Test get subject grades when student not have subject grades")
    void getOneSubjectGrades_givenStudentWithoutGrades_shouldReturnEmptyListOfGrades() {
        //given
        int studentId = 0;
        int subjectId = GradeSubject.PHYSICS.ordinal();
        given(gradeRepository.findAllByStudentIDAndGradeSubject(studentId, GradeSubject.PHYSICS)).willReturn(Optional.of(List.of()));

        //when
        SubjectResponseDTO studentGrades = gradeFacade.getOneSubjectGrades(studentId,subjectId);

        //then
        int expectedSizeOfGrades = 0;
        assertThat(studentGrades.grades(),hasSize(expectedSizeOfGrades));
    }

    @Test
    @DisplayName("Test get subject grades when student not have subject grades return correct subject weighted average")
    void getOneSubjectGrades_givenStudentWithoutGrades_shouldReturnSubjectWithZeroWeightedAverage() {
        //given
        int studentId = 0;
        int subjectId = GradeSubject.PHYSICS.ordinal();
        given(gradeRepository.findAllByStudentIDAndGradeSubject(studentId, GradeSubject.PHYSICS)).willReturn(Optional.of(List.of()));

        //when
        SubjectResponseDTO studentGrades = gradeFacade.getOneSubjectGrades(studentId,subjectId);

        //then
        String expectedAverage = "0,00";
        assertThat(studentGrades.gradeAverage(),is(equalTo(expectedAverage)));
    }

    @Test
    @DisplayName("Test get subject grades when student have grades")
    void getOneSubjectGrades_givenStudentWith4Grades_shouldReturnListOf4Grades() {
        //given
        int studentId = 0;
        int subjectId = GradeSubject.PHYSICS.ordinal();
        given(gradeRepository.findAllByStudentIDAndGradeSubject(studentId, GradeSubject.PHYSICS)).willReturn(Optional.of(
                List.of(
                        getGradeTestWeightSymbol(1,GradeSymbolValue.FIVE),
                        getGradeTestWeightSymbol(1,GradeSymbolValue.FOUR),
                        getGradeTestWeightSymbol(1,GradeSymbolValue.THREE),
                        getGradeTestWeightSymbol(2,GradeSymbolValue.NB)

                )));

        //when
        SubjectResponseDTO studentGrades = gradeFacade.getOneSubjectGrades(studentId,subjectId);

        //then
        int expectedSizeOfGrades = 4;
        assertThat(studentGrades.grades(),hasSize(expectedSizeOfGrades));
    }

    @Test
    @DisplayName("Test get subject grades when student have grades return correct subject weighted average")
    void getOneSubjectGrades_givenStudentWith3GradesWithDifferentWeights_shouldReturn_2_63_weightedAverage() {
        //given
        int studentId = 0;
        int subjectId = GradeSubject.PHYSICS.ordinal();
        given(gradeRepository.findAllByStudentIDAndGradeSubject(studentId, GradeSubject.PHYSICS)).willReturn(Optional.of(
                List.of(
                        getGradeTestWeightSymbol(3,GradeSymbolValue.FIVE), getGradeTestWeightSymbol(5,GradeSymbolValue.ONE), getGradeTestWeightSymbol(3,GradeSymbolValue.THREE)
                )
        ));

        //when
        SubjectResponseDTO studentGrades = gradeFacade.getOneSubjectGrades(studentId,subjectId);

        //then
        String expectedAverage = "2,64";
        assertThat(studentGrades.gradeAverage(),is(equalTo(expectedAverage)));
    }

    @Test
    @DisplayName("Test get subject grades when student have grades return correct subject weighted average")
    void getOneSubjectGrades_givenStudentWith3GradesWithValuesAnd3WithoutValue_shouldReturn_2_63_weightedAverage() {
        //given
        int studentId = 0;
        int subjectId = GradeSubject.PHYSICS.ordinal();
        given(gradeRepository.findAllByStudentIDAndGradeSubject(studentId, GradeSubject.PHYSICS)).willReturn(Optional.of(
                List.of(
                        getGradeTestWeightSymbol(3,GradeSymbolValue.FIVE), getGradeTestWeightSymbol(5,GradeSymbolValue.ONE), getGradeTestWeightSymbol(3,GradeSymbolValue.THREE),
                        getGradeTestWeightSymbol(1,GradeSymbolValue.NB), getGradeTestWeightSymbol(2,GradeSymbolValue.PLUS), getGradeTestWeightSymbol(5, GradeSymbolValue.MINUS)

                )));

        //when
        SubjectResponseDTO studentGrades = gradeFacade.getOneSubjectGrades(studentId,subjectId);

        //then
        String expectedAverage = "2,64";
        assertThat(studentGrades.gradeAverage(),is(equalTo(expectedAverage)));
    }

    @Test
    @DisplayName("Test get all students subject grades when student no exist")
    void getAllGradesByStudentID_givenNoExistStudent_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        given(gradeRepository.findAllByStudentID(studentId)).willReturn(Optional.empty());

        //then
        assertThrows(GradeStudentNotFoundException.class,() -> gradeFacade.getAllGradesByStudentID(studentId));
    }

    @Test
    @DisplayName("Test get all students subject grades when student no have grades")
    void getAllGradesByStudentID_givenStudentWithNoHaveGrades_shouldReturn13ListsOfGrades() {
        //given
        int studentId = 2;
            given(gradeRepository.findAllByStudentID(studentId)).willReturn(Optional.of(List.of()));


        //when
        List<SubjectResponseDTO> studentGrades = gradeFacade.getAllGradesByStudentID(studentId);


        //then
        int expectedSubjectsSize = 13;
        assertThat(studentGrades,hasSize(expectedSubjectsSize));
        for (SubjectResponseDTO grades : studentGrades) {
            assertThat(grades.grades(),hasSize(0));
        }
    }

    @Test
    @DisplayName("Test get all students subject grades when every subject have 3 grades")
    void getAllGradesByStudentID_givenStudent_whenEverySubjectHave3Grades_shouldReturn13ListsOfGradesWith3Grades() {
        //given
        int studentId = 2;
        List<Grade> grades = new ArrayList<>();
        for(GradeSubject subject : GradeSubject.values()) {
            grades.add(getGradeTestWeightSymbolSubject(3,GradeSymbolValue.FIVE,subject));
            grades.add(getGradeTestWeightSymbolSubject(3,GradeSymbolValue.ONE,subject));
            grades.add(getGradeTestWeightSymbolSubject(3,GradeSymbolValue.THREE,subject));
        }
        given(gradeRepository.findAllByStudentID(studentId)).willReturn(Optional.of(grades));

        //when
        List<SubjectResponseDTO> studentGrades = gradeFacade.getAllGradesByStudentID(studentId);

        //then
        int gradesSize = 3;
        for(SubjectResponseDTO subjectGrades : studentGrades) {
            assertThat(subjectGrades.grades(),hasSize(gradesSize));
        }
    }

    @Test
    @DisplayName("Test get all students subject grades when every subject have 3 grades return correct subject average")
    void getAllSubjectGradesByStudentID_givenStudent_whenEverySubjectHave3Grades_shouldReturn13ListsOfGradesWith_2_64_average() {
        //given
        int studentId = 2;
        List<Grade> grades = new ArrayList<>();
        for(GradeSubject subject : GradeSubject.values()) {
            grades.add(getGradeTestWeightSymbolSubject(3,GradeSymbolValue.FIVE,subject));
            grades.add(getGradeTestWeightSymbolSubject(5,GradeSymbolValue.ONE,subject));
            grades.add(getGradeTestWeightSymbolSubject(3,GradeSymbolValue.THREE,subject));
        }
        given(gradeRepository.findAllByStudentID(studentId)).willReturn(Optional.of(grades));

        //when
        List<SubjectResponseDTO> studentGrades = gradeFacade.getAllGradesByStudentID(studentId);

        //then
        String expectedAverage = "2,64";
        for(SubjectResponseDTO subjectGrades : studentGrades) {
            assertThat(subjectGrades.gradeAverage(), is(equalTo(expectedAverage)));
        }
    }


    @Test
    @DisplayName("Test get one grade by no exist student id and grade id throw exception")
    void getOneGrade_givenNoExistStudentIdOrGradeId_shouldThrowNewStudentNotFoundException() {
        //given
        int studentId = 0;
        int gradeId = 1;
        given(gradeRepository.findByStudentIDAndId(studentId,gradeId)).willReturn(Optional.empty());

        //then
        assertThrows(GradeStudentNotFoundException.class,() -> gradeFacade.getOneGrade(studentId,gradeId));

    }

    @Test
    @DisplayName("Test get one grade by no exist student id and grade id throw exception with message")
    void getOneGrade_givenNoExistStudentIdOrGradeId_shouldThrowNewStudentNotFoundExceptionWithMessage() {
        //given
        int studentId = 0;
        int gradeId = 1;
        given(gradeRepository.findByStudentIDAndId(studentId,gradeId)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(GradeStudentNotFoundException.class,() -> gradeFacade.getOneGrade(studentId,gradeId));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Student not found id: " + studentId;
        assertThat(expectedMessage,is(equalTo(actualMessage)));
    }

    @Test
    @DisplayName("Test get one grade by student id and grade id return mapped gradeViewDTO with correct fields")
    void getOneGrade_givenStudentIdAndGradeId_shouldReturnGradeViewDtoWithCorrectFields() {
        //given
        int studentId = 0;
        int gradeId = 1;
        Grade grade = getGradeTestWeightSymbol(1, GradeSymbolValue.FIVE);
        given(gradeRepository.findByStudentIDAndId(studentId,gradeId)).willReturn(Optional.of(grade));

        //when
        GradeResponeDTO actualGradeDto = gradeFacade.getOneGrade(studentId,gradeId);

        //then
        assertEquals(grade.getWeight(),actualGradeDto.weight());
        assertEquals(grade.getGradeSymbolValue().getSymbol(),actualGradeDto.degree());
        assertEquals(grade.getDescription(),actualGradeDto.description());
        assertEquals(grade.getAddedDate(),actualGradeDto.addedDate());
    }


    @Test
    @DisplayName("Test add grade to student save grade to database")
    void  addGradeToStudent_givenGradeDTO_shouldSaveGradeToDatabase() {
        //given
        GradeRequestDTO gradeRequestDTO = getGradeDto();

        //when
        gradeFacade.addGradeToStudent(gradeRequestDTO);
        Grade expectedGrade = Grade
                .builder()
                .addedBy(gradeRequestDTO.addedBy())
                .gradeSymbolValue(GradeSymbolValue.FIVE)
                .weight(4)
                .description("Klasówka")
                .addedDate(LocalDate.now())
                .gradeSubject(GradeSubject.PHYSICS)
                .studentID(gradeRequestDTO.studentID())
                .build();

        verify(gradeRepository).save(expectedGrade);
    }

    @Test
    @DisplayName("Test update grade when student or grade no exist throw exception")
    void updateGrade_givenNoExistStudentId_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        int gradeId = 0;
        GradeRequestDTO gradeRequestDTO = getGradeDto();
        given(gradeRepository.findByStudentIDAndId(studentId,gradeId)).willReturn(Optional.empty());

        //then
        assertThrows(GradeStudentNotFoundException.class,() -> gradeFacade.updateGrade(studentId,gradeId, gradeRequestDTO));
    }

    @Test
    @DisplayName("Test update grade save grade to database")
    void updateGrade_givenStudent_andTeacherId_andGradeDTO_shouldSaveDtoToDatabase() {
        //given
        int studentId = 0;
        int addingTeacherId = 0;
        int gradeId = 0;

        GradeRequestDTO gradeRequestDTO = getGradeDto();
        Grade expectedGrade = getGradeTestWeightSymbol(2,GradeSymbolValue.FIVE);
        given(gradeRepository.findByStudentIDAndId(studentId,gradeId)).willReturn(Optional.of(expectedGrade));
        given(gradeRepository.save(expectedGrade)).willReturn(expectedGrade);

        //when
        gradeFacade.updateGrade(studentId,gradeId, gradeRequestDTO);

        //then
        verify(gradeRepository).save(expectedGrade);
    }

    @Test
    @DisplayName("Test update grade change grade description, weight and degree")
    void updateGrade_givenStudent_andTeacherId_andGradeDTO_shouldChangeGradeDescription_andWeight_andDegree() {
        //given
        int studentId = 0;
        int gradeId = 0;


        int newWeight = 10;
        String newDescription = "new description";
        String newDegree = "6";
        GradeRequestDTO expectedUpdateGrade = GradeRequestDTO.builder()
                .weight(10)
                .description(newDescription)
                .addedBy("Szymon Kawka")
                .weight(newWeight)
                .subject(GradeSubject.PHYSICS.toString())
                .degree(newDegree)
                .build();

        Grade expectedGrade = getGradeTestWeightSymbol(2,GradeSymbolValue.FIVE);
        given(gradeRepository.findByStudentIDAndId(studentId,gradeId)).willReturn(Optional.of(expectedGrade));
        given(gradeRepository.save(expectedGrade)).willReturn(expectedGrade);

        //when
        GradeRequestDTO actualGrade = gradeFacade.updateGrade(studentId,gradeId,expectedUpdateGrade);

        //then
        assertEquals(expectedUpdateGrade,actualGrade);
    }

    @Test
    @DisplayName("Test update grade not change grade subject and addedBy")
    void updateGrade_givenStudent_andTeacherId_andGradeDTO_shouldNotChangeSubjectAndTeacherIDInUpdatedGrade() {
        //given
        int studentId = 0;
        int gradeId = 0;


        int newWeight = 10;
        String newDescription = "new description";
        String newDegree = "6";
        GradeRequestDTO expectedUpdateGrade = GradeRequestDTO.builder()
                .weight(10)
                .description(newDescription)
                .addedBy("Szymon Sssss")
                .weight(newWeight)
                .subject(GradeSubject.MATHS.toString())
                .degree(newDegree)
                .build();

        Grade expectedGrade = getGradeTestWeightSymbol(2,GradeSymbolValue.FIVE);
        given(gradeRepository.findByStudentIDAndId(studentId,gradeId)).willReturn(Optional.of(expectedGrade));
        given(gradeRepository.save(expectedGrade)).willReturn(expectedGrade);

        //when
        GradeRequestDTO actualGrade = gradeFacade.updateGrade(studentId,gradeId,expectedUpdateGrade);

        System.out.println(actualGrade);

        //then
        assertNotEquals(expectedUpdateGrade,actualGrade);
        assertNotEquals(expectedUpdateGrade.addedBy(),actualGrade.addedBy());
        assertEquals(GradeSubject.PHYSICS.toString(),actualGrade.subject());
    }

    @Test
    @DisplayName("Test delete grade when student or grade no exist throw exception")
    void deleteStudentGrade_givenNoExistStudentIdOrGradeId_shouldThrowStudentNotFoundException() {
        //given
        int studentId = 0;
        int gradeId = 0;
        given(gradeRepository.findByStudentIDAndId(studentId,gradeId)).willReturn(Optional.empty());

        //then
        assertThrows(GradeStudentNotFoundException.class,() -> gradeFacade.deleteStudentGrade(studentId,gradeId));
    }

    @Test
    @DisplayName("Test delete grade delete grade from database")
    void deleteStudentGrade_givenStudentId_andGradeId_shouldDeleteGradeFromDatabase() {
        //given
        int studentId = 0;
        int gradeId = 0;
        Grade grade = getGradeTestWeightSymbol(2,GradeSymbolValue.FIVE);
        given(gradeRepository.findByStudentIDAndId(studentId,gradeId)).willReturn(Optional.of(grade));

        //when
        gradeFacade.deleteStudentGrade(studentId,gradeId);

        //then
        verify(gradeRepository).delete(grade);
    }

}
