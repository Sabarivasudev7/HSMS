package com.hsms.user_service.service;

import java.util.List;

import com.hsms.user_service.model.TechnicianProfileDto;

public interface TechnicianProfileService {
	
	TechnicianProfileDto createTechnician(Long userId, TechnicianProfileDto dto);
    TechnicianProfileDto getById(Long id);
    List<TechnicianProfileDto> getAll();
    TechnicianProfileDto update(Long id, TechnicianProfileDto dto);
    void delete(Long id);
}
