package com.assignment.accountValidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.assignment.accountValidate")

public class AccountValidateApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountValidateApplication.class, args);
	}

}
