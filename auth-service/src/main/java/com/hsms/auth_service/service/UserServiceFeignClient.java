package com.hsms.auth_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hsms.auth_service.model.UserRegistrationDto;
import com.hsms.auth_service.security.FeignConfig;

@FeignClient(name = "USER-SERVICE",configuration = FeignConfig.class)
public interface UserServiceFeignClient {

	@PostMapping("/api/users")
	String createUser(
            @RequestBody UserRegistrationDto dto,
            @RequestHeader("X-Role") String role
            
    );
}