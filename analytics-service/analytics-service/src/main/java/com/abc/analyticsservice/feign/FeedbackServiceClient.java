package com.abc.analyticsservice.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "feedback-service",
        url = "http://localhost:8089"
)
public interface FeedbackServiceClient {

    @GetMapping("/api/feedback")
    List<Object> getAllFeedbacks(@RequestHeader("X-Role") String role);

}