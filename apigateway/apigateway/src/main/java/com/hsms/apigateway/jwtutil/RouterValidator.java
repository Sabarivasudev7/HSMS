package com.hsms.apigateway.jwtutil;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class RouterValidator {

    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/eureka",
            "/actuator"
    );

    private final Predicate<HttpServletRequest> securedRoute = request -> OPEN_ENDPOINTS.stream()
            .noneMatch(uri -> request.getRequestURI().startsWith(uri));

    public boolean isSecured(HttpServletRequest request) {
        return securedRoute.test(request);
    }
}
