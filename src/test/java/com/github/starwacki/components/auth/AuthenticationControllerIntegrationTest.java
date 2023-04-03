package com.github.starwacki.components.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import com.github.starwacki.components.auth.dto.AuthenticationResponse;
import com.github.starwacki.common.model.account.Parent;
import com.github.starwacki.common.model.account.Role;
import com.github.starwacki.common.model.account.Student;
import com.github.starwacki.common.model.account.Teacher;
import com.github.starwacki.common.repositories.ParentRepository;
import com.github.starwacki.common.repositories.StudentRepository;
import com.github.starwacki.common.repositories.TeacherRepository;
import com.github.starwacki.common.security.AES;
import com.github.starwacki.common.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Test authenticate exist student return 200 HTTP status, add cookie with jwt and return jwt with correct claims")
    void authenticate_givenExistStudent_shouldReturn_200_HTTPStatus_andReturnJWTCookieInHTTPResponse_andReturnJWTWithCorrectClaims() throws Exception {

        //given
        String username = "usernameSTU1";
        String password = "123456";

        Student student = Student.builder()
                .firstname("firstname")
                .lastname("lastname")
                .username(username)
                .role(Role.STUDENT)
                .password(AES.encrypt(password))
                .build();
        int studentId = studentRepository.save(student).getId();
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .username(username)
                .password(password)
                .build();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/auth/authenticate")
                .content(objectMapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedJWT = jwtService.generateToken(Map.of("id",studentId),student);
        String actualJWT= objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AuthenticationResponse.class).token();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getCookie("jwt").getValue(), is(equalTo(actualJWT))
                ));
       assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.getSubject()),
               is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.getSubject()))));
       assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.get("id")),
               is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.get("id")))));
    }

    @Test
    @DisplayName("Test authenticate exist teacher return 200 HTTP status, add cookie with jwt and return jwt with correct claims")
    void authenticate_givenExistTeacher_shouldReturn_200_HTTPStatus_andReturnJWTCookieInHTTPResponse_andReturnJWTWithCorrectClaims() throws Exception {

        //given
        String username = "usernameNAU1";
        String password = "123456";

        Teacher teacher = Teacher.builder()
                .firstname("firstname")
                .lastname("lastname")
                .username(username)
                .role(Role.STUDENT)
                .password(AES.encrypt(password))
                .build();
        int teacherId = teacherRepository.save(teacher).getId();
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .username(username)
                .password(password)
                .build();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/auth/authenticate")
                .content(objectMapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedJWT = jwtService.generateToken(Map.of("id",teacherId),teacher);
        String actualJWT= objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AuthenticationResponse.class).token();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getCookie("jwt").getValue(), is(equalTo(actualJWT))
                ));
        assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.getSubject()),
                is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.getSubject()))));
        assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.get("id")),
                is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.get("id")))));
    }

    @Test
    @DisplayName("Test authenticate exist parent return 200 HTTP status, add cookie with jwt and return jwt with correct claims")
    void authenticate_givenExistParent_shouldReturn_200_HTTPStatus_andReturnJWTCookieInHTTPResponse_andReturnJWTWithCorrectClaims() throws Exception {

        //given
        String username = "usernameRO1";
        String password = "123456";

        Parent parent = Parent.builder()
                .firstname("firstname")
                .lastname("lastname")
                .username(username)
                .role(Role.STUDENT)
                .password(AES.encrypt(password))
                .build();
        int parentId = parentRepository.save(parent).getId();
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .username(username)
                .password(password)
                .build();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/auth/authenticate")
                .content(objectMapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedJWT = jwtService.generateToken(Map.of("id",parentId),parent);
        String actualJWT= objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AuthenticationResponse.class).token();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getCookie("jwt").getValue(), is(equalTo(actualJWT))
                ));
        assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.getSubject()),
                is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.getSubject()))));
        assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.get("id")),
                is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.get("id")))));
    }

    @ParameterizedTest
    @ValueSource(strings = {"usernameRO1","usernameSTU1","usernameNAU1","username"})
    @DisplayName("Test authenticate no exist account return 401 HTTP status and return error message")
    void authenticate_givenNoExistAccount_shouldReturn_200_HTTPStatus_andReturnErrorMessage(String username) throws Exception {

        //given
        String password = "123456";

        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .username(username)
                .password(password)
                .build();

        //when
        ResultActions resultActions  = mockMvc.perform(post("/auth/authenticate")
                .content(objectMapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String exceptedErrorMessage = "Wrong username or password";
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(exceptedErrorMessage))));
    }




}
