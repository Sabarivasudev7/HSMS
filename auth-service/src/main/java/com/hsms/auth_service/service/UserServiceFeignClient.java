package com.hsms.auth_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hsms.auth_service.model.UserRegistrationDto;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceFeignClient {

    @PostMapping("/api/users")
    String createUser(@RequestBody UserRegistrationDto dto);
}