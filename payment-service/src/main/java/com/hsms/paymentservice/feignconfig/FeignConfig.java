package com.hsms.paymentservice.feignconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeignConfig {
	
	@Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {template.header("X-Role", "ADMIN");};
    }

}