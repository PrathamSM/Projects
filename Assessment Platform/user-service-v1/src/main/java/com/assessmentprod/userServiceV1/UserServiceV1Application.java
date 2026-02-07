package com.assessmentprod.userServiceV1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients
public class UserServiceV1Application {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceV1Application.class, args);
	}

}
