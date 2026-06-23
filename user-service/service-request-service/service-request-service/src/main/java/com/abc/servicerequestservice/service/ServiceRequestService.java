package com.abc.servicerequestservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abc.servicerequestservice.dto.ServiceRequestDetailResponse;
import com.abc.servicerequestservice.dto.ServiceRequestRequest;
import com.abc.servicerequestservice.dto.ServiceRequestUpdateDTO;
import com.abc.servicerequestservice.enums.RequestStatus;

public interface ServiceRequestService {

	ServiceRequestDetailResponse createRequest(ServiceRequestRequest request);

	ServiceRequestDetailResponse getRequestById(Long requestId);

	List<ServiceRequestDetailResponse> getAllRequests();

	Page<ServiceRequestDetailResponse> getRequestsByCustomer(Long customerId, Pageable pageable);

	ServiceRequestDetailResponse updateRequest(Long requestId, ServiceRequestUpdateDTO requestDTO);

	ServiceRequestDetailResponse updateRequestStatus(Long requestId, RequestStatus status);

	void cancelRequest(Long requestId);
}