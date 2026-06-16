package com.hsms.auth_service;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hsms.auth_service.security.JwtFilter;
import com.hsms.auth_service.security.JwtUtil;

@SpringBootApplication
@EnableFeignClients
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception {

	    return config.getAuthenticationManager();
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,JwtFilter jwtFilter) throws Exception {

	    http
	            .csrf(csrf -> csrf.disable())
	            .sessionManagement(session ->
	                    session.sessionCreationPolicy(
	                            SessionCreationPolicy.STATELESS))
	            .authorizeHttpRequests(auth -> auth
	                    .requestMatchers(
	                            "/api/auth/**",
	                            "/swagger-ui/**",
	                            "/v3/api-docs/**",
	                            "/actuator/**")
	                    .permitAll()
	                    .anyRequest()
	                    .authenticated())
	            .addFilterBefore(
	                    jwtFilter,
	                    UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

}
