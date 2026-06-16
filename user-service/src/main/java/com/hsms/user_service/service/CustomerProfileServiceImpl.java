package com.hsms.user_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hsms.user_service.entity.CustomerProfile;
import com.hsms.user_service.exception.CustomerNotFoundException;
import com.hsms.user_service.model.CustomerProfileDto;
import com.hsms.user_service.repository.CustomerProfileRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final CustomerProfileRepository repository;
    private final ModelMapper mapper;

    @Override
    public CustomerProfileDto createCustomer(Long userId, CustomerProfileDto dto) {
        CustomerProfile customer = mapper.map(dto, CustomerProfile.class);
        customer.setCustomerId(userId); 
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

        customer.setName(dto.getName());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setPincode(dto.getPincode());

        return mapper.map(repository.save(customer), CustomerProfileDto.class);
    }

    @Override
    public void delete(Long id) {
        CustomerProfile customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
        repository.delete(customer);
    }
}
