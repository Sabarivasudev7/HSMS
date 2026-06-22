package com.hsms.user_service.service;

import java.util.List;
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

    private static final String NOT_FOUND = " not found";
    private static final String USER_WITH_ID = "User with id ";
    private static final String CUSTOMER_WITH_ID = "Customer with id ";

    private final CustomerProfileRepository repository;
    private final UserProfileRepository userProfileRepository;
    private final ModelMapper mapper;

    @Override
    public CustomerProfileDto createCustomer(Long userId, CustomerProfileDto dto) {

        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        USER_WITH_ID + userId + NOT_FOUND));

        if (!"CUSTOMER".equalsIgnoreCase(userProfile.getRole())) {
            throw new RoleMismatchException(
                    "You are trying to create a Customer profile, but the registered role is "
                            + userProfile.getRole());
        }

        CustomerProfile customer = mapper.map(dto, CustomerProfile.class);
        customer.setCustomerId(userId);
        customer.setUserProfile(userProfile);

        return mapper.map(repository.save(customer), CustomerProfileDto.class);
    }

    @Override
    public CustomerProfileDto getById(Long id) {
        CustomerProfile customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        CUSTOMER_WITH_ID + id + NOT_FOUND));

        return mapper.map(customer, CustomerProfileDto.class);
    }

    @Override
    public List<CustomerProfileDto> getAll() {
        return repository.findAll().stream()
                .map(c -> mapper.map(c, CustomerProfileDto.class))
                .toList();
    }

    @Override
    public CustomerProfileDto update(Long id, CustomerProfileDto dto) {
        CustomerProfile customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        CUSTOMER_WITH_ID + id + NOT_FOUND));

        mapper.map(dto, customer);
        return mapper.map(repository.save(customer), CustomerProfileDto.class);
    }

    @Override
    public void delete(Long id) {
        CustomerProfile customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        CUSTOMER_WITH_ID + id + NOT_FOUND));

        repository.delete(customer);
    }
}