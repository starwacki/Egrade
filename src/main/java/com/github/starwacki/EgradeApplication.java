package com.github.starwacki;

import com.github.starwacki.global.security.AES;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class EgradeApplication {


	public static void main(String[] args) throws Exception {
		SpringApplication.run(EgradeApplication.class, args);
		System.out.println(AES.decrypt("9EN/BfYkR6LAty6KN66l7w=="));

	}


}
