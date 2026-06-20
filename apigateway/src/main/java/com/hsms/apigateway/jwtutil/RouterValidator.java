package com.hsms.apigateway.jwtutil;

import java.util.List;
import java.util.function.Predicate;
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

    public Predicate<HttpServletRequest> isSecured =request -> openApiEndpoints.stream().noneMatch(uri -> request.getRequestURI().contains(uri));

}