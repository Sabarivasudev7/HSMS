package com.abc.executionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExecutionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExecutionServiceApplication.class, args);
	}

}
