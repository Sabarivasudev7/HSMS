package com.hsms.user_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hsms.user_service.entity.CustomerProfile;
import com.hsms.user_service.entity.UserProfile;
import com.hsms.user_service.exception.CustomerNotFoundException;
import com.hsms.user_service.exception.RoleMismatchException;
import com.hsms.user_service.model.CustomerProfileDto;
import com.hsms.user_service.repository.CustomerProfileRepository;
import com.hsms.user_service.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final CustomerProfileRepository repository;
    private final UserProfileRepository userProfileRepository;
    private final ModelMapper mapper;

    @Override
    public CustomerProfileDto createCustomer(Long userId, CustomerProfileDto dto) {
       
        UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new CustomerNotFoundException("User with id " + userId + " not found"));
       
        if (!"CUSTOMER".equalsIgnoreCase(userProfile.getRole())) {
            throw new RoleMismatchException(
                "You are trying to create a Customer profile, but the registered role is " + userProfile.getRole()
            );
        }
        CustomerProfile customer = mapper.map(dto, CustomerProfile.class);
        customer.setCustomerId(userId);
        customer.setUserProfile(userProfile); 

        return mapper.map(repository.save(customer), CustomerProfileDto.class);
    }


    @Override
    public CustomerProfileDto getById(Long id) {
        CustomerProfile customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
        return mapper.map(customer, CustomerProfileDto.class);
    }

    @Override
    public List<CustomerProfileDto> getAll() {
        return repository.findAll().stream().map(c -> mapper.map(c, CustomerProfileDto.class)).collect(Collectors.toList());
    }

    @Override
    public CustomerProfileDto update(Long id, CustomerProfileDto dto) {
        CustomerProfile customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));

        mapper.map(dto, customer); 
        return mapper.map(repository.save(customer), CustomerProfileDto.class);
    }

    @Override
    public void delete(Long id) {
        CustomerProfile customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
        repository.delete(customer);
    }
}
