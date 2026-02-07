package com.smeportal.proexpservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ProfessionalExperienceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfessionalExperienceServiceApplication.class, args);
	}

}
