package com.github.starwacki.components.teacher;


import com.github.starwacki.components.teacher.TeacherDTO;
import com.github.starwacki.components.teacher.SchoolClassDTO;
import com.github.starwacki.components.teacher.TeacherRepository;
import com.github.starwacki.components.teacher.TeacherService;
import com.github.starwacki.components.teacher.exceptions.SchoolClassNotFoundException;
import com.github.starwacki.components.teacher.exceptions.TeacherNotFoundException;
import com.github.starwacki.common.model.grades.Subject;
import com.github.starwacki.common.model.school_class.SchoolClass;
import com.github.starwacki.common.repositories.SchoolClassRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AccountTeacherServiceUnitTest {

//    @InjectMocks
//    private TeacherService teacherService;
//    @Mock
//    private TeacherRepository teacherRepository;
//    @Mock
//    private SchoolClassRepository schoolClassRepository;
//
//    @Test
//    @DisplayName("Test  get teacher classes when teacher has 2 classes return list with 2 DTO")
//    void  getTeacherClasses_givenTeacherId_returnListWithTwoElements() {
//        //given
//        Set<SchoolClass> teacherClasses = Set.of(
//                new SchoolClass("1A",2023),new SchoolClass("2A",2024)
//        );
//        AccountTeacher accountTeacher = AccountTeacher
//                .builder()
//                .firstname("firstname")
//                .lastname("lastname")
//                .role(Role.TEACHER)
//                .email("email@wp.pl")
//                .password("password")
//                .workPhone("111222333")
//                .build();
//        accountTeacher.setClasses(teacherClasses);
//        given(teacherRepository.findTeacherById(anyInt())).willReturn(accountTeacher);
//
//        //when
//        List<SchoolClassDTO> classDTOList = teacherService.getTeacherClasses(1);
//
//        //then
//        assertThat(classDTOList,hasSize(2));
//    }
//
//
//    @Test
//    @DisplayName("Test  get teacher classes mapping SchoolClass to SchoolClassDTO")
//    void  getTeacherClasses_givenTeacherIdAndSchoolClass_returnListWithSameFieldsLikeGivenSchoolClasses() {
//        //given
//        SchoolClass schoolClass =   new SchoolClass("1A",2023);
//        Set<SchoolClass> teacherClasses = Set.of(schoolClass);
//        AccountTeacher accountTeacher = AccountTeacher
//                .builder()
//                .firstname("firstname")
//                .lastname("lastname")
//                .role(Role.TEACHER)
//                .email("email@wp.pl")
//                .password("password")
//                .workPhone("111222333")
//                .build();
//        accountTeacher.setClasses(teacherClasses);
//        given(teacherRepository.findTeacherById(anyInt())).willReturn(accountTeacher);
//
//        //when
//        List<SchoolClassDTO> classDTOList = teacherService.getTeacherClasses(1);
//
//        //then
//        SchoolClassDTO schoolClassDTO = classDTOList.get(0);
//        assertThat(schoolClassDTO.className(),is(equalTo(schoolClass.getName())));
//        assertThat(schoolClassDTO.year(),is(equalTo(schoolClass.getClassYear())));
//    }
//
//    @Test
//    @DisplayName("Test add school class to no exist teacher throw TeacherNotFoundException")
//    void addSchoolClassToTeacher_givenNoExistTeacherIdAndSchoolClass_shouldThrowTeacherNotFoundException() {
//        //given
//        int teacherId = 1;
//        SchoolClassDTO classDTO = SchoolClassDTO
//                .builder()
//                .year(2024)
//                .className("3A")
//                .build();
//        given(teacherRepository.findById(1)).willReturn(Optional.empty());
//
//        //then
//        assertThrows(TeacherNotFoundException.class,() -> teacherService.addSchoolClassToTeacher(teacherId,classDTO));
//    }
//
//    @Test
//    @DisplayName("Test add school class to no exist teacher throw TeacherNotFoundException with message")
//    void addSchoolClassToTeacher_givenNoExistTeacherIdAndSchoolClass_shouldThrowTeacherNotFoundExceptionWthMessage() {
//        //given
//        int teacherId = 1;
//        SchoolClassDTO classDTO = SchoolClassDTO
//                .builder()
//                .year(2024)
//                .className("3A")
//                .build();
//        given(teacherRepository.findById(1)).willReturn(Optional.empty());
//
//        //when
//        Exception exception = assertThrows(TeacherNotFoundException.class,() -> teacherService.addSchoolClassToTeacher(teacherId,classDTO));
//
//        //then
//        String expectedExceptionMessage =  "Not found teacher with id: " + teacherId;
//        assertThat(expectedExceptionMessage,is(equalTo(exception.getMessage())));
//    }
//
//    @Test
//    @DisplayName("Test add no exist school class to teacher throw SchoolClassNotFoundException")
//    void addSchoolClassToTeacher_givenTeacherIdAndNoExistSchoolClass_shouldThrowSchoolClassNotFoundException() {
//        //given
//        int teacherId = 1;
//        String className = "3A";
//        int year = 2024;
//        SchoolClassDTO classDTO = SchoolClassDTO
//                .builder()
//                .year(2024)
//                .className("3A")
//                .build();
//        given(teacherRepository.findById(1)).willReturn(Optional.of(mock(AccountTeacher.class)));
//        given(schoolClassRepository.findSchoolClassByNameAndAndClassYear(className,year)).willReturn(Optional.empty());
//
//        //then
//        assertThrows(SchoolClassNotFoundException.class,() -> teacherService.addSchoolClassToTeacher(teacherId,classDTO));
//    }
//
//    @Test
//    @DisplayName("Test add no exist school class teacher throw SchoolClassNotFoundException with message")
//    void addSchoolClassToTeacher_givenTeacherIdAndNoExistSchoolClass_shouldThrowSchoolClassNotFoundExceptionWithMessage() {
//        //given
//        int teacherId = 1;
//        SchoolClassDTO classDTO = SchoolClassDTO
//                .builder()
//                .year(2024)
//                .className("3A")
//                .build();
//        given(teacherRepository.findById(1)).willReturn(Optional.empty());
//
//        //when
//        Exception exception = assertThrows(TeacherNotFoundException.class,() -> teacherService.addSchoolClassToTeacher(teacherId,classDTO));
//
//        //then
//        String expectedExceptionMessage =  "Not found teacher with id: " + teacherId;
//        assertThat(expectedExceptionMessage,is(equalTo(exception.getMessage())));
//    }
//
//    @Test
//    @DisplayName("Test get all teachers mapping Teacher To TeacherDTO")
//    void getAllTeachers_shouldReturnListWithTeacherDTO() {
//        //given
//        AccountTeacher accountTeacher = AccountTeacher
//                .builder()
//                .firstname("firstname")
//                .lastname("lastname")
//                .workPhone("111222333")
//                .password("password")
//                .subject(Subject.PHYSICS)
//                .email("email@wp.pl")
//                .build();
//        given(teacherRepository.findAll()).willReturn(List.of(accountTeacher));
//
//        //when
//        List<TeacherDTO> teacherDTOS = teacherService.getAllTeachers();
//
//        //then
//        TeacherDTO teacherDTO = teacherDTOS.get(0);
//        assertThat(teacherDTO.email(),is(equalTo(accountTeacher.getEmail())));
//        assertThat(teacherDTO.firstname(),is(equalTo(accountTeacher.getFirstname())));
//        assertThat(teacherDTO.lastname(),is(equalTo(accountTeacher.getLastname())));
//        assertThat(teacherDTO.phone(),is(equalTo(accountTeacher.getWorkPhone())));
//        assertThat(teacherDTO.subject(),is(equalTo(accountTeacher.getSubject().toString())));
//    }
//
//
//

}
