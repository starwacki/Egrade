package com.github.starwacki.components.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import com.github.starwacki.components.auth.dto.AuthenticationResponse;
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
    private AuthAccountAuthQueryRepository authAccountAuthQueryRepository;
    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Test authenticate exist student return 200 HTTP status, add cookie with jwt and return jwt with correct claims")
    void authenticate_givenExistStudentAuthAccountDetails_shouldReturn_200_HTTPStatus_andReturnJWTCookieInHTTPResponse_andReturnJWTWithCorrectClaims() throws Exception {

        //given
        String username = "usernameSTU1";
        String password = "123456";

        AuthAccountDetails accountStudent = AuthAccountDetails.builder()
                .username(username)
                .accountRole("STUDENT")
                .password(AuthenticationAESAlgorithm.encrypt(password))
                .build();
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
        String expectedJWT = jwtService.generateToken(Map.of("ROLE","STUDENT"), accountStudent);
        String actualJWT= objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AuthenticationResponse.class).token();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getCookie("jwt").getValue(), is(equalTo(actualJWT))
                ));
       assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.getSubject()),
               is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.getSubject()))));
       assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.get("ROLE")),
               is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.get("ROLE")))));
    }

    @Test
    @DisplayName("Test authenticate exist teacher return 200 HTTP status, add cookie with jwt and return jwt with correct claims")
    void authenticate_givenExistTeacherAuthAccountDetails_shouldReturn_200_HTTPStatus_andReturnJWTCookieInHTTPResponse_andReturnJWTWithCorrectClaims() throws Exception {

        //given
        String username = "usernameNAU1";
        String password = "123456";

        AuthAccountDetails accountTeacher = AuthAccountDetails.builder()
                .username(username)
                .accountRole("TEACHER")
                .password(AuthenticationAESAlgorithm.encrypt(password))
                .build();
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
        String expectedJWT = jwtService.generateToken(Map.of("ROLE","TEACHER"), accountTeacher);
        String actualJWT= objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AuthenticationResponse.class).token();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getCookie("jwt").getValue(), is(equalTo(actualJWT))
                ));
        assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.getSubject()),
                is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.getSubject()))));
        assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.get("ROLE")),
                is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.get("ROLE")))));
    }

    @Test
    @DisplayName("Test authenticate exist parent return 200 HTTP status, add cookie with jwt and return jwt with correct claims")
    void authenticate_givenExistParentAccountAuthDetails_shouldReturn_200_HTTPStatus_andReturnJWTCookieInHTTPResponse_andReturnJWTWithCorrectClaims() throws Exception {

        //given
        String username = "usernameRO1";
        String password = "123456";

        AuthAccountDetails accountParent = AuthAccountDetails.builder()
                .username(username)
                .accountRole("PARENT")
                .password(AuthenticationAESAlgorithm.encrypt(password))
                .build();
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
        String expectedJWT = jwtService.generateToken(Map.of("ROLE","PARENT"),accountParent);
        String actualJWT= objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), AuthenticationResponse.class).token();
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getCookie("jwt").getValue(), is(equalTo(actualJWT))
                ));
        assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.getSubject()),
                is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.getSubject()))));
        assertThat(jwtService.extractClaim(expectedJWT,claims -> claims.get("ROLE")),
                is(equalTo(jwtService.extractClaim(actualJWT,claims -> claims.get("ROLE")))));
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
