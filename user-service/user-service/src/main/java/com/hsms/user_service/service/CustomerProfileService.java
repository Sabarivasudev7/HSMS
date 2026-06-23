package com.hsms.user_service.service;

import java.util.List;

import com.hsms.user_service.model.CustomerProfileDto;

public interface CustomerProfileService {
	 CustomerProfileDto createCustomer(Long userId, CustomerProfileDto dto);
	    CustomerProfileDto getById(Long id);
	    List<CustomerProfileDto> getAll();
	    CustomerProfileDto update(Long id, CustomerProfileDto dto);
	    
}