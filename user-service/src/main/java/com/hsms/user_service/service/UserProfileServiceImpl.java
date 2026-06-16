package com.hsms.user_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hsms.user_service.entity.UserProfile;
import com.hsms.user_service.exception.UserNotFoundException;
import com.hsms.user_service.model.UserProfileDto;
import com.hsms.user_service.model.UserRegistrationDto;
import com.hsms.user_service.repository.UserProfileRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository repository;
    private final ModelMapper mapper;

    @Override
    public void createUserFromAuth(UserRegistrationDto dto) {

        UserProfile user = mapper.map(dto, UserProfile.class);
        
        if ("CUSTOMER".equalsIgnoreCase(dto.getRole())) {
            user.setCustomerId(dto.getUserId());
        } else if ("TECHNICIAN".equalsIgnoreCase(dto.getRole())) {
            user.setTechnicianId(dto.getUserId()); 
        }
        
        repository.save(user);
    }

    @Override
    public UserProfileDto getById(Long id) {
        UserProfile user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        return mapper.map(user, UserProfileDto.class);
    }

    @Override
    public List<UserProfileDto> getAll() {
        return repository.findAll().stream().map(user -> mapper.map(user, UserProfileDto.class)).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        UserProfile user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        repository.delete(user);
    }
}
