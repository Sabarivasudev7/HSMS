package com.hsms.execution_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ExecutionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExecutionServiceApplication.class, args);
	}

}
