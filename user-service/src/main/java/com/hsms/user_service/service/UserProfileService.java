package com.hsms.user_service.service;

import java.util.List;

import com.hsms.user_service.model.UserProfileDto;
import com.hsms.user_service.model.UserRegistrationDto;

public interface UserProfileService {
	
	void createUserFromAuth(UserRegistrationDto dto);
    UserProfileDto getById(Long id);
    List<UserProfileDto> getAll();
    void delete(Long id);
}