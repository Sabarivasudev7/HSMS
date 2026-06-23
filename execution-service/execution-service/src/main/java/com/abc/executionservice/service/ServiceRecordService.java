package com.abc.executionservice.service;

import com.abc.executionservice.dto.ServiceRecordDetailResponseDTO;
import com.abc.executionservice.dto.ServiceRecordRequestDTO;
import com.abc.executionservice.dto.ServiceRecordResponseDTO;

import java.util.List;

public interface ServiceRecordService {

    ServiceRecordResponseDTO createServiceRecord(ServiceRecordRequestDTO requestDTO, Long userId);

    ServiceRecordDetailResponseDTO getServiceRecordById(Long recordId);

    List<ServiceRecordDetailResponseDTO> getAllServiceRecords();

    ServiceRecordResponseDTO updateServiceRecord(Long recordId, ServiceRecordRequestDTO requestDTO, Long userId);

    void deleteServiceRecord(Long recordId);
}