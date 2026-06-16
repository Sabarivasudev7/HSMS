package com.hsms.user_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hsms.user_service.entity.TechnicianProfile;
import com.hsms.user_service.exception.TechnicianNotFoundException;
import com.hsms.user_service.model.TechnicianProfileDto;
import com.hsms.user_service.repository.TechnicianProfileRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechnicianProfileServiceImpl implements TechnicianProfileService {

    private final TechnicianProfileRepository repository;
    private final ModelMapper mapper;

    @Override
    public TechnicianProfileDto createTechnician(Long userId, TechnicianProfileDto dto) {
        TechnicianProfile technician = mapper.map(dto, TechnicianProfile.class);
        technician.setTechnicianId(userId);
        return mapper.map(repository.save(technician), TechnicianProfileDto.class);
    }

    @Override
    public TechnicianProfileDto getById(Long id) {
        TechnicianProfile technician = repository.findById(id).orElseThrow(() -> new TechnicianNotFoundException("Technician with id " + id + " not found"));
        return mapper.map(technician, TechnicianProfileDto.class);
    }

    @Override
    public List<TechnicianProfileDto> getAll() {
        return repository.findAll().stream().map(t -> mapper.map(t, TechnicianProfileDto.class)).collect(Collectors.toList());
    }

    @Override
    public TechnicianProfileDto update(Long id, TechnicianProfileDto dto) {
        TechnicianProfile technician = repository.findById(id).orElseThrow(() -> new TechnicianNotFoundException("Technician with id " + id + " not found"));

        technician.setSkill(dto.getSkill());
        technician.setExperience(dto.getExperience());
        technician.setPhone(dto.getPhone());
        technician.setAvailability(dto.getAvailability());
        technician.setRating(dto.getRating());

        return mapper.map(repository.save(technician), TechnicianProfileDto.class);
    }

    @Override
    public void delete(Long id) {
        TechnicianProfile technician = repository.findById(id).orElseThrow(() -> new TechnicianNotFoundException("Technician with id " + id + " not found"));
        repository.delete(technician);
    }
}
