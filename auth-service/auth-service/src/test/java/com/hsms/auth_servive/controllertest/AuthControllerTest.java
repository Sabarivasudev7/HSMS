package com.hsms.auth_servive.controllertest;

import com.hsms.auth_service.controller.AuthController;
import com.hsms.auth_service.model.AuthResponse;
import com.hsms.auth_service.model.LoginRequest;
import com.hsms.auth_service.model.RegisterRequest;
import com.hsms.auth_service.model.RegisterResponse;
import com.hsms.auth_service.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthService authService;

	@InjectMocks
	private AuthController authController;

	private MockMvc mockMvc;

	@Test
	void registerSuccess() throws Exception {
	    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	    RegisterResponse response = new RegisterResponse(1L, "User Registered Successfully");
	    when(authService.register(any(RegisterRequest.class))).thenReturn(response);

	    mockMvc.perform(post("/api/auth/register")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content("{\"name\":\"John\",\"email\":\"john@example.com\",\"password\":\"password\"}"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.userId").value(1))
	            .andExpect(jsonPath("$.message").value("User Registered Successfully"));
	}


	@Test
	void loginSuccess() throws Exception {
	    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	    AuthResponse response = new AuthResponse("token", "CUSTOMER", "John");
	    when(authService.login(any(LoginRequest.class))).thenReturn(response);

	    mockMvc.perform(post("/api/auth/login")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content("{\"email\":\"john@example.com\",\"password\":\"password\"}"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.token").value("token"))
	            .andExpect(jsonPath("$.role").value("CUSTOMER"))
	            .andExpect(jsonPath("$.name").value("John"));
	}

}
