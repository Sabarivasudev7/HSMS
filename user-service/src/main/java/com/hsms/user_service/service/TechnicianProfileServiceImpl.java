package com.hsms.user_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hsms.user_service.entity.TechnicianProfile;
import com.hsms.user_service.entity.UserProfile;
import com.hsms.user_service.exception.TechnicianNotFoundException;
import com.hsms.user_service.exception.RoleMismatchException;
import com.hsms.user_service.exception.UserNotFoundException;
import com.hsms.user_service.model.TechnicianProfileDto;
import com.hsms.user_service.repository.TechnicianProfileRepository;
import com.hsms.user_service.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechnicianProfileServiceImpl implements TechnicianProfileService {

    private final TechnicianProfileRepository repository;
    private final UserProfileRepository userProfileRepository;
    private final ModelMapper mapper;

    @Override
    public TechnicianProfileDto createTechnician(Long userId, TechnicianProfileDto dto) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        if (!"TECHNICIAN".equalsIgnoreCase(userProfile.getRole())) {
            throw new RoleMismatchException(
                "You are trying to create a Technician profile, but the registered role is " + userProfile.getRole()
            );
        }

        TechnicianProfile technician = mapper.map(dto, TechnicianProfile.class);
        technician.setTechnicianId(userId);
        technician.setUserProfile(userProfile);

        return mapper.map(repository.save(technician), TechnicianProfileDto.class);
    }

    @Override
    public TechnicianProfileDto getById(Long id) {
        TechnicianProfile technician = repository.findById(id)
                .orElseThrow(() -> new TechnicianNotFoundException("Technician with id " + id + " not found"));
        return mapper.map(technician, TechnicianProfileDto.class);
    }

    @Override
    public List<TechnicianProfileDto> getAll() {
        return repository.findAll()
                .stream()
                .map(t -> mapper.map(t, TechnicianProfileDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TechnicianProfileDto update(Long id, TechnicianProfileDto dto) {
        TechnicianProfile technician = repository.findById(id)
                .orElseThrow(() -> new TechnicianNotFoundException("Technician with id " + id + " not found"));
        mapper.map(dto, technician);
        return mapper.map(repository.save(technician), TechnicianProfileDto.class);
    }

    @Override
    public void delete(Long id) {
        TechnicianProfile technician = repository.findById(id)
                .orElseThrow(() -> new TechnicianNotFoundException("Technician with id " + id + " not found"));
        repository.delete(technician);
    }
}
