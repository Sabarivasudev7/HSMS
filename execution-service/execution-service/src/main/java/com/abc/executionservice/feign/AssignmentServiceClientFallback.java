package com.abc.executionservice.feign;

import org.springframework.stereotype.Component;

@Component
public class AssignmentServiceClientFallback implements AssignmentServiceClient {

    @Override
    public Object getAssignmentByTechnicianId(Long technicianId) {
        return null;
    }
}