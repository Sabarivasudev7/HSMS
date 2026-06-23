package com.abc.analyticsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8082"
)
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}")
    Object getUserProfile(@PathVariable("userId") Long userId, @RequestHeader("X-Role") String role);

}
