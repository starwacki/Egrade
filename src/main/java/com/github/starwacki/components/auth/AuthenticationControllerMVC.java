package com.github.starwacki.components.auth;

import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import com.github.starwacki.components.auth.dto.AuthenticationResponse;
import com.github.starwacki.components.auth.exceptions.WrongAuthenticationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/egrade")
class AuthenticationControllerMVC {

    private final AuthenticationController authenticationController;
    

    @GetMapping("/login")
    String loginPage(@ModelAttribute AuthenticationRequest request, Model model) {
        model.addAttribute("request",request);
        return "login";
    }

    @PostMapping("/login")
    String postPage(@ModelAttribute AuthenticationRequest request, Model model, HttpServletResponse response) {
        model.addAttribute("request",request);
        try {
            ResponseEntity<AuthenticationResponse> authenticationResponse =  authenticationController.authenticate(request,response);
            return "mainview";
        } catch (WrongAuthenticationException authenticationException) {
            return "redirect:/egrade/login?error";
        }
    }



}
