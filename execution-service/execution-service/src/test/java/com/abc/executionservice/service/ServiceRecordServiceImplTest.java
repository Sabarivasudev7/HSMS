package com.abc.executionservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.abc.executionservice.dto.ServiceRecordRequestDTO;
import com.abc.executionservice.dto.ServiceRecordResponseDTO;
import com.abc.executionservice.dto.ServiceRequestResponseDTO;
import com.abc.executionservice.entity.ServiceRecord;
import com.abc.executionservice.exception.InvalidStateTransitionException;
import com.abc.executionservice.exception.ServiceRecordNotFoundException;
import com.abc.executionservice.feign.ServiceRequestClient;
import com.abc.executionservice.repository.ServiceRecordRepository;

@ExtendWith(MockitoExtension.class)
public class ServiceRecordServiceImplTest {

    @Mock
    private ServiceRecordRepository serviceRecordRepository;

    @Mock
    private ServiceRequestClient serviceRequestClient;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private ServiceRecordServiceImpl serviceRecordService;

    private ServiceRecordRequestDTO requestDTO;
    private ServiceRequestResponseDTO requestResponseDTO;
    private ServiceRecord serviceRecord;

    @BeforeEach
    public void setup() {
        requestDTO = new ServiceRecordRequestDTO();
        requestDTO.setServiceRequestId(20L);
        requestDTO.setRemarks("Some remarks");
        requestDTO.setActualCost(150.0);

        requestResponseDTO = new ServiceRequestResponseDTO();
        requestResponseDTO.setRequestId(20L);
        requestResponseDTO.setStatus("ACCEPTED");
        requestResponseDTO.setTechnicianId(10L);

        serviceRecord = new ServiceRecord();
        serviceRecord.setId(100L);
        serviceRecord.setServiceRequestId(20L);
        serviceRecord.setTechnicianId(10L);
        serviceRecord.setStartTime(LocalDateTime.now().minusHours(1));
        serviceRecord.setRemarks("Started");
        serviceRecord.setActualCost(0.0);
    }

    @Test
    public void testStartService_Success() {
        when(serviceRequestClient.getServiceRequestById("TECHNICIAN", 20L))
                .thenReturn(requestResponseDTO);
        when(serviceRecordRepository.findByServiceRequestId(20L))
                .thenReturn(Optional.empty());
        when(serviceRecordRepository.save(any(ServiceRecord.class)))
                .thenAnswer(invocation -> {
                    ServiceRecord records = invocation.getArgument(0);
                    records.setId(100L);
                    return records;
                });

        ServiceRecordResponseDTO response = serviceRecordService.createServiceRecord(requestDTO, 10L);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(20L, response.getServiceRequestId());
        assertEquals(10L, response.getTechnicianId());
        assertNotNull(response.getStartTime());
        verify(serviceRequestClient).updateServiceRequestStatus("TECHNICIAN", 20L, "IN_PROGRESS");
    }

    @Test
    public void testStartService_InvalidState_ThrowsException() {
        requestResponseDTO.setStatus("ASSIGNED");
        when(serviceRequestClient.getServiceRequestById("TECHNICIAN", 20L))
                .thenReturn(requestResponseDTO);

        assertThrows(InvalidStateTransitionException.class, () -> {
            serviceRecordService.createServiceRecord(requestDTO, 10L);
        });
    }

    @Test
    public void testStartService_TechnicianMismatch_ThrowsException() {
        when(serviceRequestClient.getServiceRequestById("TECHNICIAN", 20L))
                .thenReturn(requestResponseDTO);

        assertThrows(InvalidStateTransitionException.class, () -> {
            serviceRecordService.createServiceRecord(requestDTO, 11L); // 11 != 10
        });
    }

    @Test
    public void testStartService_AlreadyStarted_ThrowsException() {
        when(serviceRequestClient.getServiceRequestById("TECHNICIAN", 20L))
                .thenReturn(requestResponseDTO);
        when(serviceRecordRepository.findByServiceRequestId(20L))
                .thenReturn(Optional.of(serviceRecord));

        assertThrows(InvalidStateTransitionException.class, () -> {
            serviceRecordService.createServiceRecord(requestDTO, 10L);
        });
    }

    @Test
    public void testCompleteService_Success() {
        requestResponseDTO.setStatus("IN_PROGRESS");
        when(serviceRecordRepository.findById(100L))
                .thenReturn(Optional.of(serviceRecord));
        when(serviceRequestClient.getServiceRequestById("TECHNICIAN", 20L))
                .thenReturn(requestResponseDTO);
        when(serviceRecordRepository.save(any(ServiceRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ServiceRecordResponseDTO response = serviceRecordService.updateServiceRecord(100L, requestDTO, 10L);

        assertNotNull(response);
        assertEquals("Some remarks", response.getRemarks());
        assertEquals(150.0, response.getActualCost());
        assertNotNull(response.getEndTime());
        verify(serviceRequestClient).updateServiceRequestStatus("TECHNICIAN", 20L, "COMPLETED");
    }

    @Test
    public void testCompleteService_InvalidState_ThrowsException() {
        requestResponseDTO.setStatus("ACCEPTED"); // Should be IN_PROGRESS
        when(serviceRecordRepository.findById(100L))
                .thenReturn(Optional.of(serviceRecord));
        when(serviceRequestClient.getServiceRequestById("TECHNICIAN", 20L))
                .thenReturn(requestResponseDTO);

        assertThrows(InvalidStateTransitionException.class, () -> {
            serviceRecordService.updateServiceRecord(100L, requestDTO, 10L);
        });
    }

    @Test
    public void testCompleteService_TechnicianMismatch_ThrowsException() {
        requestResponseDTO.setStatus("IN_PROGRESS");
        when(serviceRecordRepository.findById(100L))
                .thenReturn(Optional.of(serviceRecord));
        when(serviceRequestClient.getServiceRequestById("TECHNICIAN", 20L))
                .thenReturn(requestResponseDTO);

        assertThrows(InvalidStateTransitionException.class, () -> {
            serviceRecordService.updateServiceRecord(100L, requestDTO, 11L); // 11 != 10
        });
    }

    @Test
    public void testCompleteService_RecordNotFound_ThrowsException() {
        when(serviceRecordRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(ServiceRecordNotFoundException.class, () -> {
            serviceRecordService.updateServiceRecord(100L, requestDTO, 10L);
        });
    }
}
