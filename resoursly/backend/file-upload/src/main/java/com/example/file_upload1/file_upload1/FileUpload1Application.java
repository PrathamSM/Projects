package com.example.file_upload1.file_upload1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FileUpload1Application {

	public static void main(String[] args) {
		SpringApplication.run(FileUpload1Application.class, args);
	}

}
