package com.abc.assignmentservice.feign;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.abc.assignmentservice.dto.ServiceRequestDetailResponseDTO;
import com.abc.assignmentservice.enums.Roles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServiceRequestClientFallbackFactory implements FallbackFactory<ServiceRequestClient> {

    @Override
    public ServiceRequestClient create(Throwable cause) {
        return new ServiceRequestClient() {
            @Override
            public ResponseEntity<ServiceRequestDetailResponseDTO> getRequestById(Long id, Roles role) {
                log.error("Feign failure in getRequestById for id: {}, role: {}. Reason: ", id, role, cause);
                return ResponseEntity.ok(new ServiceRequestDetailResponseDTO());
            }

            @Override
            public ResponseEntity<ServiceRequestDetailResponseDTO> updateStatus(Long id, String status, Roles role) {
                log.error("Feign failure in updateStatus for id: {}, status: {}, role: {}. Reason: ", id, status, role, cause);
                return ResponseEntity.ok(new ServiceRequestDetailResponseDTO());
            }
        };
    }
}
