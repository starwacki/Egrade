package com.github.starwacki.common.mvc;

import com.github.starwacki.components.account.AccountFacade;
import com.github.starwacki.components.auth.AuthenticationFacade;
import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
class MVCLoginController {

    private AuthenticationFacade authenticationFacade;

    @GetMapping("/login/p")
    String loginPage(@ModelAttribute AuthenticationRequest request, Model model) {
        model.addAttribute("request",request);
        return "login";
    }

    @PostMapping("/login")
    String postPage(@ModelAttribute AuthenticationRequest request, Model model) {
        model.addAttribute("request",request);
        return "login";
    }

    @GetMapping("/mainpage")
    String mainPage() {
        return "mainview";
    }

}
