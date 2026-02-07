package com.smeportal.project_team_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProjectTeamServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectTeamServiceApplication.class, args);
	}

}
