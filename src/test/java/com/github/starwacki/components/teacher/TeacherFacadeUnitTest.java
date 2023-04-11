package com.github.starwacki.components.teacher;


import com.github.starwacki.components.teacher.dto.TeacherResponseDTO;
import com.github.starwacki.components.teacher.dto.TeacherSchoolClassDTO;
import com.github.starwacki.components.teacher.exceptions.TeacherNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TeacherFacadeUnitTest {

    @InjectMocks
    private TeacherFacade teacherFacade;
    @Mock
    private TeacherRepository teacherRepository;

    @Test
    @DisplayName("Test  get teacher classes when teacher has 2 classes return list with 2 DTO")
    void  getTeacherClasses_givenTeacherId_returnListWithTwoElements() {
        //given
        int teacherId = 1;
        Set<TeacherSchoolClass> teacherClasses = Set.of(
                TeacherSchoolClass.builder().className("1A").classYear(2023).build(),
                TeacherSchoolClass.builder().className("1A").classYear(2024).build()
        );
        Teacher teacher = Teacher
                .builder()
                .id(teacherId)
                .teacherSchoolClass(teacherClasses)
                .workPhone("11122333")
                .email("email@wp.pl")
                .firstname("Teacher")
                .lastname("Teacherlastname")
                .build();
        given(teacherRepository.findTeacherById(teacherId)).willReturn(Optional.of(teacher));

        //when
        List<TeacherSchoolClassDTO> classDTOList = teacherFacade.getTeacherClasses(teacherId);

        //then
        assertThat(classDTOList,hasSize(2));
    }


    @Test
    @DisplayName("Test  get teacher classes mapping TeacherSchoolClass to TeacherSchoolClassDTO")
    void  getTeacherClasses_givenTeacherIdAndSchoolClass_returnListWithSameFieldsLikeGivenSchoolClasses() {
        //given
        int teacherId = 1;
        TeacherSchoolClass schoolClass = TeacherSchoolClass.builder().className("1A").classYear(2023).build();
        Set<TeacherSchoolClass> teacherClasses = Set.of(
                schoolClass
        );
        Teacher teacher = Teacher
                .builder()
                .id(teacherId)
                .teacherSchoolClass(teacherClasses)
                .workPhone("11122333")
                .email("email@wp.pl")
                .firstname("Teacher")
                .lastname("Teacherlastname")
                .build();
        given(teacherRepository.findTeacherById(teacherId)).willReturn(Optional.of(teacher));

        //when
        List<TeacherSchoolClassDTO> classDTOList = teacherFacade.getTeacherClasses(teacherId);

        //then
        TeacherSchoolClassDTO schoolClassDTO = classDTOList.get(0);
        assertThat(schoolClassDTO.className(),is(equalTo(schoolClass.getClassName())));
        assertThat(schoolClassDTO.year(),is(equalTo(schoolClass.getClassYear())));
    }

    @Test
    @DisplayName("Test add school class to no exist teacher throw TeacherNotFoundException")
    void addSchoolClassToTeacher_givenNoExistTeacherIdAndSchoolClass_shouldThrowTeacherNotFoundException() {
        //given
        int teacherId = 1;
        TeacherSchoolClassDTO classDTO = TeacherSchoolClassDTO
                .builder()
                .year(2024)
                .className("3A")
                .build();
        given(teacherRepository.findById(teacherId)).willReturn(Optional.empty());

        //then
        assertThrows(TeacherNotFoundException.class,() -> teacherFacade.addSchoolClassToTeacher(teacherId,classDTO));
    }

    @Test
    @DisplayName("Test add school class to no exist teacher throw TeacherNotFoundException with message")
    void addSchoolClassToTeacher_givenNoExistTeacherIdAndSchoolClass_shouldThrowTeacherNotFoundExceptionWthMessage() {
        //given
        int teacherId = 1;
        TeacherSchoolClassDTO classDTO = TeacherSchoolClassDTO
                .builder()
                .year(2024)
                .className("3A")
                .build();
        given(teacherRepository.findById(teacherId)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(TeacherNotFoundException.class,() -> teacherFacade.addSchoolClassToTeacher(teacherId,classDTO));

        //then
        String expectedExceptionMessage =  "Not found teacher with id: " + teacherId;
        assertThat(expectedExceptionMessage,is(equalTo(exception.getMessage())));
    }

    @Test
    @DisplayName("Test get all teachers mapping Teacher To TeacherDTO")
    void getAllTeachers_shouldReturnListWithTeacherDTO() {
        //given
        Teacher accountTeacher = Teacher
                .builder()
                .firstname("firstname")
                .lastname("lastname")
                .workPhone("111222333")
                .subject("PHYSICS")
                .email("email@wp.pl")
                .build();
        given(teacherRepository.findAll()).willReturn(List.of(accountTeacher));

        //when
        List<TeacherResponseDTO> teacherDTOS = teacherFacade.getAllTeachers();

        //then
        TeacherResponseDTO teacherDTO = teacherDTOS.get(0);
        assertThat(teacherDTO.email(),is(equalTo(accountTeacher.getEmail())));
        assertThat(teacherDTO.firstname(),is(equalTo(accountTeacher.getFirstname())));
        assertThat(teacherDTO.lastname(),is(equalTo(accountTeacher.getLastname())));
        assertThat(teacherDTO.phone(),is(equalTo(accountTeacher.getWorkPhone())));
        assertThat(teacherDTO.subject(),is(equalTo(accountTeacher.getSubject())));
    }




}
