package com.hsms.apigateway.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hsms.apigateway.jwtutil.JwtUtil;
import com.hsms.apigateway.jwtutil.MutableHttpServletRequest;
import com.hsms.apigateway.jwtutil.RouterValidator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final RouterValidator validator;

    public AuthenticationFilter(JwtUtil jwtUtil, RouterValidator validator) {
        this.jwtUtil = jwtUtil;
        this.validator = validator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (!validator.isSecured(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing token");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        jwtUtil.validateToken(token);

        MutableHttpServletRequest mutatedRequest = new MutableHttpServletRequest(request);
        mutatedRequest.putHeader("X-User", jwtUtil.extractUsername(token));
        mutatedRequest.putHeader("X-User-Id", String.valueOf(jwtUtil.extractUserId(token)));
        mutatedRequest.putHeader("X-Role", jwtUtil.extractRole(token));

        filterChain.doFilter(mutatedRequest, response);
    }
}
