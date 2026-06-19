package com.hsms.user_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hsms.user_service.entity.CustomerProfile;
import com.hsms.user_service.entity.TechnicianProfile;
import com.hsms.user_service.entity.UserProfile;
import com.hsms.user_service.exception.UserNotFoundException;
import com.hsms.user_service.model.CustomerProfileDto;
import com.hsms.user_service.model.TechnicianProfileDto;
import com.hsms.user_service.model.UserProfileDto;
import com.hsms.user_service.model.UserRegistrationDto;
import com.hsms.user_service.repository.CustomerProfileRepository;
import com.hsms.user_service.repository.TechnicianProfileRepository;
import com.hsms.user_service.repository.UserProfileRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userRepo;
    private final CustomerProfileRepository customerRepo;
    private final TechnicianProfileRepository technicianRepo;
    private final ModelMapper mapper;

    @Override
    public void createUserFromAuth(UserRegistrationDto dto) {
        UserProfile profile = mapper.map(dto, UserProfile.class);
        userRepo.save(profile);

        if ("CUSTOMER".equals(dto.getRole())) {
            CustomerProfile customer = new CustomerProfile();
            customer.setCustomerId(profile.getUserId());
            customer.setUserProfile(profile);
            customerRepo.save(customer);
        } else if ("TECHNICIAN".equals(dto.getRole())) {
            TechnicianProfile tech = new TechnicianProfile();
            tech.setTechnicianId(profile.getUserId());
            tech.setUserProfile(profile); 
            technicianRepo.save(tech);
        }
    }

    @Override
    public UserProfileDto getById(Long id) {
        return userRepo.findById(id).map(profile -> mapper.map(profile, UserProfileDto.class)).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<UserProfileDto> getAll() {
        return userRepo.findAll().stream().map(profile -> {
                UserProfileDto dto = mapper.map(profile, UserProfileDto.class);

                if ("CUSTOMER".equals(profile.getRole())) {
                    customerRepo.findById(profile.getUserId()).ifPresent(c -> dto.setCustomerProfile(mapper.map(c, CustomerProfileDto.class)));
                } else if ("TECHNICIAN".equals(profile.getRole())) {
                    technicianRepo.findById(profile.getUserId()).ifPresent(t -> dto.setTechnicianProfile(mapper.map(t, TechnicianProfileDto.class)));
                }

                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}
