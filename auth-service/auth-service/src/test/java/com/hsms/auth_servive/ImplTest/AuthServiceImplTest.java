package com.hsms.auth_servive.ImplTest;

import com.hsms.auth_service.entity.AuthUser;
import com.hsms.auth_service.enums.Role;
import com.hsms.auth_service.exception.InvalidCredentialsException;
import com.hsms.auth_service.exception.UserAlreadyExistsException;
import com.hsms.auth_service.exception.UserNotFoundException;
import com.hsms.auth_service.model.AuthResponse;
import com.hsms.auth_service.model.LoginRequest;
import com.hsms.auth_service.model.RegisterRequest;
import com.hsms.auth_service.model.RegisterResponse;
import com.hsms.auth_service.repository.UserRepository;
import com.hsms.auth_service.security.JwtUtil;
import com.hsms.auth_service.service.AuthServiceImpl;
import com.hsms.auth_service.service.UserServiceFeignClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private UserServiceFeignClient userServiceFeignClient;

	@InjectMocks
	private AuthServiceImpl authService;

	@Test
	void registerSuccess() {
		RegisterRequest request = new RegisterRequest("John", "john@example.com", "password", Role.CUSTOMER);
		when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");
		AuthUser savedUser = new AuthUser();
		savedUser.setId(1L);
		savedUser.setName("John");
		savedUser.setEmail("john@example.com");
		savedUser.setPassword("encoded");
		savedUser.setRole(Role.CUSTOMER);
		savedUser.setEnabled(true);
		when(userRepository.save(any(AuthUser.class))).thenReturn(savedUser);

		RegisterResponse response = authService.register(request);

		assertEquals(1L, response.getUserId());
		assertEquals("User Registered Successfully", response.getMessage());
		verify(userServiceFeignClient).createUser(any(), eq("CUSTOMER"));
	}

	@Test
	void registerThrowsUserAlreadyExistsException() {
		RegisterRequest request = new RegisterRequest("John", "john@example.com", "password", Role.CUSTOMER);
		when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
		assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
	}

	@Test
	void loginSuccess() {
		LoginRequest request = new LoginRequest("john@example.com", "password");
		AuthUser user = new AuthUser();
		user.setId(1L);
		user.setName("John");
		user.setEmail("john@example.com");
		user.setPassword("encoded");
		user.setRole(Role.CUSTOMER);
		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
		UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
				.password(user.getPassword()).authorities(user.getRole().name()).build();
		when(jwtUtil.generateToken(userDetails, user.getId(), user.getRole().name())).thenReturn("token");

		AuthResponse response = authService.login(request);

		assertEquals("token", response.getToken());
		assertEquals("CUSTOMER", response.getRole());
		assertEquals("John", response.getName());
	}

	@Test
	void loginThrowsUserNotFoundException() {
		LoginRequest request = new LoginRequest("john@example.com", "password");
		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> authService.login(request));
	}

	@Test
	void loginThrowsInvalidCredentialsException() {
		LoginRequest request = new LoginRequest("john@example.com", "wrong");
		AuthUser user = new AuthUser();
		user.setId(1L);
		user.setName("John");
		user.setEmail("john@example.com");
		user.setPassword("encoded");
		user.setRole(Role.CUSTOMER);
		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);
		assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
	}
}
