package com.hsms.execution_service.Service;

import com.hsms.execution_service.DTO.ExecutionResponseDTO;

public interface ExecutionService {
	
	ExecutionResponseDTO acceptBooking(Long requestId);
	
	ExecutionResponseDTO rejectBooking(Long requestId);

	ExecutionResponseDTO startService(Long requestId);

	ExecutionResponseDTO completeService(Long requestId);

}
