package com.github.starwacki.components.auth;

import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import com.github.starwacki.components.auth.dto.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
interface AuthenticationOperations {

    @Operation(
            summary = "Authenticate user",
            description = "Operation authenticate user, return jwt token and add JWT Cookie ",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(ref = "authenticateUserRequestBody"),
            responses = {
                    @ApiResponse(responseCode = "200",ref = "okAuthenticateUserResponse"),
                    @ApiResponse(responseCode = "401", ref = "unauthorizedAuthenticateUserResponse"),
                    @ApiResponse (responseCode = "400", ref = "badRequestAuthenticateUserResponse"),
            }
    )
    @PostMapping("/authenticate")
    ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request,
            HttpServletResponse response
    );
}
