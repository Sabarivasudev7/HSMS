package com.hsms.apigateway.jwtutil;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


@Component
public class RouterValidator {
	 
    public static final List<String> openApiEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/eureka",
            "/actuator"
           );

    public boolean isSecured(HttpServletRequest request) {
        return openApiEndpoints.stream().noneMatch(uri -> request.getRequestURI().contains(uri));
    }
}