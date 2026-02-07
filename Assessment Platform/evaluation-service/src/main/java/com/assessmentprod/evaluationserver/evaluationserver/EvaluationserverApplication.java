package com.assessmentprod.evaluationserver.evaluationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = "com.assessmentprod.evaluationserver.evaluationserver.feignclient")
@SpringBootApplication
public class EvaluationserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvaluationserverApplication.class, args);
	}

}
