package com.github.starwacki.student.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.student.controller.StudentController;
import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.components.student.service.StudentGradeService;
import com.github.starwacki.components.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
 class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private StudentGradeService studentGradeService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDTO prepareStudentDTO(String className,int year) {
        return StudentDTO
                .builder()
                .id(1)
                .firstname("firstname")
                .lastname("lastname")
                .parentPhone("111222333")
                .className(className)
                .year(year)
                .build();
    }

    @Test
    void getAllStudentsFromClass_givenClassNameAndClassYear_shouldReturnOkStatus() throws Exception {
        //given
        int year = 2023;
        String className = "2A";
        List<StudentDTO> studentDTOS = List.of(
                prepareStudentDTO(className,year),
                prepareStudentDTO(className,year),
                prepareStudentDTO(className,year));
        given(studentService.getAllStudentsFromClass(className,year)).willReturn(studentDTOS);

        //when
        ResultActions response = mockMvc.perform(get("/students/"+className+"/"+year)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllStudentsFromClass_givenClassNameAndClassYear_shouldReturnNotFoundStatus() throws Exception {
        //given
        int year = 2023;
        String className = "2A";
        List<StudentDTO> studentDTOS = List.of(
                prepareStudentDTO(className,year),
                prepareStudentDTO(className,year),
                prepareStudentDTO(className,year));
        given(studentService.getAllStudentsFromClass(className,year)).willReturn(studentDTOS);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/students/"+className+"/"+year)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        //then
        System.out.println(objectMapper.writeValueAsString(studentDTOS));
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


}
