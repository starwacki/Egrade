package com.github.starwacki.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(
        jsr250Enabled = true
)
public class MethodSecurityConfig {
}
