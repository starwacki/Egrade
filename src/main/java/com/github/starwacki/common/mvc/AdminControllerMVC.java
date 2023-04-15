package com.github.starwacki.common.mvc;

import com.github.starwacki.components.account.AccountController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
class AdminControllerMVC {

    private final AccountController accountController;

    @GetMapping
    String adminMainView() {
        return "adminDasboard";
    }
}
