package com.abc.executionservice.service;

import com.abc.executionservice.dto.*;
import com.abc.executionservice.entity.ServiceRecord;
import com.abc.executionservice.exception.InvalidStateTransitionException;
import com.abc.executionservice.exception.ServiceRecordNotFoundException;
import com.abc.executionservice.feign.ServiceRequestClient;
import com.abc.executionservice.repository.ServiceRecordRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceRecordServiceImpl implements ServiceRecordService {

    
    private final ServiceRecordRepository serviceRecordRepository;

    
    private final ServiceRequestClient serviceRequestClient;

    
    private final ModelMapper modelMapper;

    // US-10: Start Service — only ACCEPTED requests can be started
    @Override
    public ServiceRecordResponseDTO createServiceRecord(ServiceRecordRequestDTO requestDTO, Long userId) {
        if (userId == null) {
            throw new InvalidStateTransitionException("Technician ID is required to start service.");
        }

        ServiceRequestResponseDTO serviceRequest =
                serviceRequestClient.getServiceRequestById("TECHNICIAN", requestDTO.getServiceRequestId());

        if (serviceRequest == null || "UNAVAILABLE".equals(serviceRequest.getStatus())) {
            throw new InvalidStateTransitionException("Service request is unavailable or does not exist.");
        }

        if (!"ACCEPTED".equals(serviceRequest.getStatus())) {
            throw new InvalidStateTransitionException(
                    "Service can only be started when request status is ACCEPTED.");
        }

        if (serviceRequest.getTechnicianId() == null || !serviceRequest.getTechnicianId().equals(userId)) {
            throw new InvalidStateTransitionException("Technician is not assigned to this service request.");
        }

        serviceRecordRepository.findByServiceRequestId(requestDTO.getServiceRequestId())
                .ifPresent(r -> {
                    throw new InvalidStateTransitionException("Service has already been started/recorded for this request.");
                });

        // Propagate status update to service-request-service
        serviceRequestClient.updateServiceRequestStatus("TECHNICIAN", requestDTO.getServiceRequestId(), "IN_PROGRESS");

        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setServiceRequestId(requestDTO.getServiceRequestId());
        serviceRecord.setTechnicianId(userId);
        serviceRecord.setStartTime(LocalDateTime.now());
        serviceRecord.setActualCost(0.0);
        serviceRecord.setRemarks(requestDTO.getRemarks());

        ServiceRecord saved = serviceRecordRepository.save(serviceRecord);

        return modelMapper.map(saved, ServiceRecordResponseDTO.class);
    }

    @Override
    public ServiceRecordDetailResponseDTO getServiceRecordById(Long recordId) {
        ServiceRecord serviceRecord = serviceRecordRepository.findById(recordId)
                .orElseThrow(() -> new ServiceRecordNotFoundException(
                        "Service Record is not found with : " + recordId));

        ServiceRecordDetailResponseDTO dto = new ServiceRecordDetailResponseDTO();
        dto.setRecordId(serviceRecord.getId());
        dto.setServiceRequestId(serviceRecord.getServiceRequestId());
        dto.setTechnicianId(serviceRecord.getTechnicianId());
        dto.setStartTime(serviceRecord.getStartTime());
        dto.setEndTime(serviceRecord.getEndTime());
        dto.setRemarks(serviceRecord.getRemarks());
        dto.setActualCost(serviceRecord.getActualCost());
        return dto;
    }

    @Override
    public List<ServiceRecordDetailResponseDTO> getAllServiceRecords() {
        return serviceRecordRepository.findAll()
                .stream()
                .map(records -> {
                    ServiceRecordDetailResponseDTO dto = new ServiceRecordDetailResponseDTO();
                    dto.setRecordId(records.getId());
                    dto.setServiceRequestId(records.getServiceRequestId());
                    dto.setTechnicianId(records.getTechnicianId());
                    dto.setStartTime(records.getStartTime());
                    dto.setEndTime(records.getEndTime());
                    dto.setRemarks(records.getRemarks());
                    dto.setActualCost(records.getActualCost());
                    return dto;
                }).toList();
              
    }

    // US-11: Complete Service — only IN_PROGRESS requests can be completed
    @Override
    public ServiceRecordResponseDTO updateServiceRecord(Long recordId,
                                                        ServiceRecordRequestDTO requestDTO,
                                                        Long userId) {
        if (userId == null) {
            throw new InvalidStateTransitionException("Technician ID is required to complete service.");
        }

        ServiceRecord existingRecord = serviceRecordRepository.findById(recordId)
                .orElseThrow(() -> new ServiceRecordNotFoundException(
                        "ServiceRecord not found with this ID: " + recordId));

        ServiceRequestResponseDTO serviceRequest =
                serviceRequestClient.getServiceRequestById("TECHNICIAN", existingRecord.getServiceRequestId());

        if (serviceRequest == null || "UNAVAILABLE".equals(serviceRequest.getStatus())) {
            throw new InvalidStateTransitionException("Service request is unavailable or does not exist.");
        }

        if (!"IN_PROGRESS".equals(serviceRequest.getStatus())) {
            throw new InvalidStateTransitionException(
                    "Service can only be completed when request status is IN_PROGRESS. " +
                    "Current status: " + serviceRequest.getStatus());
        }

        if (serviceRequest.getTechnicianId() == null || !serviceRequest.getTechnicianId().equals(userId)) {
            throw new InvalidStateTransitionException("Technician is not assigned to this service request.");
        }

        // Propagate status update to service-request-service
        serviceRequestClient.updateServiceRequestStatus("TECHNICIAN", existingRecord.getServiceRequestId(), "COMPLETED");

        existingRecord.setEndTime(LocalDateTime.now());
        existingRecord.setRemarks(requestDTO.getRemarks());
        existingRecord.setActualCost(requestDTO.getActualCost() != null ? requestDTO.getActualCost() : 0.0);

        ServiceRecord updated = serviceRecordRepository.save(existingRecord);

        return modelMapper.map(updated, ServiceRecordResponseDTO.class);
    }

    @Override
    public void deleteServiceRecord(Long recordId) {
        ServiceRecord serviceRecord = serviceRecordRepository.findById(recordId)
                .orElseThrow(() -> new ServiceRecordNotFoundException(
                        "There is no Service_Record with ID: " + recordId));
        serviceRecordRepository.delete(serviceRecord);
    }
}