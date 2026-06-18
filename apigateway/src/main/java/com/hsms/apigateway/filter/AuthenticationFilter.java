package com.hsms.apigateway.filter;

import java.io.IOException;
import java.util.List;

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

	private final JwtUtil jwtUtil;
	private final RouterValidator validator;

	public AuthenticationFilter(JwtUtil jwtUtil, RouterValidator validator) {
		this.jwtUtil = jwtUtil;
		this.validator = validator;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!validator.isSecured.test(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Missing token");
			return;
		}

		String token = authHeader.substring(7);

		try {
			jwtUtil.validateToken(token);

			String username = jwtUtil.extractUsername(token);
			Long userId = jwtUtil.extractUserId(token);
			List<String> roles = jwtUtil.extractRoles(token);

			String rolesHeader = String.join(",", roles);

			MutableHttpServletRequest mutatedRequest = new MutableHttpServletRequest(request);

			mutatedRequest.putHeader("X-User", username);
			mutatedRequest.putHeader("X-User-Id", String.valueOf(userId));
			mutatedRequest.putHeader("X-Roles", rolesHeader);

			filterChain.doFilter(mutatedRequest, response);
			return;

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid token");
		}
	}
}