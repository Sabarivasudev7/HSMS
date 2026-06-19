package com.hsms.auth_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
               
                String serviceToken = "Bearer " + System.getenv("SERVICE_JWT");
                requestTemplate.header("Authorization", serviceToken);
            }
        };
    }
}