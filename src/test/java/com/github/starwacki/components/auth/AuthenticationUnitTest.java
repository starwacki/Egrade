package com.github.starwacki.components.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import com.github.starwacki.components.auth.dto.AuthenticationResponse;
import com.github.starwacki.components.auth.exceptions.WrongAuthenticationException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthenticationUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test authenticate given blank username return 400 HTTP status")
    void authenticate_givenBlankUsername_shouldReturn_400_HTTPStatus() throws Exception {

        //given
        String username = "";
        String password = "123456";
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .username(username)
                .password(password)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/auth/authenticate")
                .content(objectMapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Test authenticate given blank username return 400 HTTP status")
    void authenticate_givenBlankPassword_shouldReturn_400_HTTPStatus() throws Exception {

        //given
        String username = "username";
        String password = "";
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .username(username)
                .password(password)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/auth/authenticate")
                .content(objectMapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Test authenticate given empty request body return 400 HTTP status")
    void authenticate_givenEmptyRequestBody_shouldReturn_400_HTTPStatus() throws Exception {

        //given

        //when
        ResultActions resultActions = mockMvc.perform(post("/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Test authenticate given no exist account return 401 HTTP status")
    void authenticate_givenNoExistAccount_shouldReturn_401_HTTPStatus() throws Exception {

        //given
        String username = "username";
        String password = "password";
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .username(username)
                .password(password)
                .build();
        given(authenticationService.authenticate(authenticationRequest)).willThrow(new WrongAuthenticationException());

        //when
        ResultActions resultActions = mockMvc.perform(post("/auth/authenticate")
                .content(objectMapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @DisplayName("Test authenticate given exist account return 200 HTTP status and JWT in Response")
    void authenticate_givenExistAccount_shouldReturn_200_HTTPStatus_andJWTInResponse() throws Exception {

        //given
        String username = "username";
        String password = "password";
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .username(username)
                .password(password)
                .build();
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiU3R1ZGVudFRlc3RTVFUxIiwiaWF0IjoxNjc5MTQ4NjAwLCJleHAiOjE2NzkyMzUwMDB9.mxQlCANbW-cGuFfLtz59BDD0AewrUKWiNcp1jPTKFNU";
        AuthenticationResponse response = new AuthenticationResponse(token);
        Cookie cookie = new Cookie("jwt",response.token());
        given(authenticationService.authenticate(authenticationRequest)).willReturn(response);
        given(authenticationService.generateJWTCookie(response.token())).willReturn(cookie);

        //when
        ResultActions resultActions = mockMvc.perform(post("/auth/authenticate")
                .content(objectMapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        String expectedResponseBody = objectMapper.writeValueAsString(response);
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(),
                        is(equalTo(expectedResponseBody))));
    }

    @Test
    @DisplayName("Test authenticate given exist account return 200 HTTP status and JWT in Response")
    void authenticate_givenExistAccount_shouldReturn_200_HTTPStatus_andAddJWTCookieToResponse() throws Exception {

        //given
        String username = "username";
        String password = "password";
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .username(username)
                .password(password)
                .build();
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiU3R1ZGVudFRlc3RTVFUxIiwiaWF0IjoxNjc5MTQ4NjAwLCJleHAiOjE2NzkyMzUwMDB9.mxQlCANbW-cGuFfLtz59BDD0AewrUKWiNcp1jPTKFNU";
        AuthenticationResponse response = new AuthenticationResponse(token);
        Cookie cookie = new Cookie("jwt",response.token());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24*1000*7);
        cookie.setPath("/");
        cookie.setSecure(true);
        given(authenticationService.authenticate(authenticationRequest)).willReturn(response);
        given(authenticationService.generateJWTCookie(response.token())).willReturn(cookie);

        //when
        ResultActions resultActions = mockMvc.perform(post("/auth/authenticate")
                .content(objectMapper.writeValueAsString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> assertThat(result.getResponse().getCookie("jwt"),
                        is(equalTo(cookie))));
    }
}
