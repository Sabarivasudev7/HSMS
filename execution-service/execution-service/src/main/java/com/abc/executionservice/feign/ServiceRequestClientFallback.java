package com.abc.executionservice.feign;

import com.abc.executionservice.dto.ServiceRequestResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ServiceRequestClientFallback implements ServiceRequestClient {

    @Override
    public ServiceRequestResponseDTO getServiceRequestById(String role, Long requestId) {
        ServiceRequestResponseDTO fallback = new ServiceRequestResponseDTO();
        fallback.setRequestId(requestId);
        fallback.setStatus("UNAVAILABLE");
        fallback.setTechnicianId(null);
        return fallback;
    }

    @Override
    public ServiceRequestResponseDTO updateServiceRequestStatus(String role, Long requestId, String status) {
        ServiceRequestResponseDTO fallback = new ServiceRequestResponseDTO();
        fallback.setRequestId(requestId);
        fallback.setStatus("UNAVAILABLE");
        fallback.setTechnicianId(null);
        return fallback;
    }
}