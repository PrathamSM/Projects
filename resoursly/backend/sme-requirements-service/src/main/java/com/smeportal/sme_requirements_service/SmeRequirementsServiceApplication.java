package com.smeportal.sme_requirements_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SmeRequirementsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmeRequirementsServiceApplication.class, args);
	}
}
