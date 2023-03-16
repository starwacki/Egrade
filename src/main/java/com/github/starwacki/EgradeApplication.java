package com.github.starwacki;

import com.github.starwacki.global.security.AES;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@SecurityScheme(name = "BearerJWT",type = SecuritySchemeType.HTTP,scheme = "bearer",bearerFormat = "JWT")
public class EgradeApplication {


	public static void main(String[] args)  {
		SpringApplication.run(EgradeApplication.class, args);
	}


}
