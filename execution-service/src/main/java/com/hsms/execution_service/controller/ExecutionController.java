package com.hsms.execution_service.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hsms.execution_service.DTO.ExecutionResponseDTO;
import com.hsms.execution_service.Service.ExecutionService;


@RestController
@RequestMapping("/api/execution")
public class ExecutionController {
	
	@Autowired
	private ExecutionService executionService; 
	
	
	
	// Technician can accept assigned jobs
    // Status updated to ACCEPTED
	@PutMapping("/accept/{requestId}")
	public ResponseEntity<ExecutionResponseDTO> acceptBooking(@PathVariable Long requestId) {
	    return ResponseEntity.status(HttpStatus.SC_OK).body(executionService.acceptBooking(requestId));
	}
		
	
	
	
	// Technician can reject assigned jobs
	// Status updated to REJECTED
	@PutMapping("/reject/{requestId}")
	public ResponseEntity<ExecutionResponseDTO> rejectBooking(@PathVariable Long requestId) {
		return ResponseEntity.status(HttpStatus.SC_OK).body(executionService.rejectBooking(requestId));
	}
	
	
	
		// Only ACCEPTED requests can be started
	// Status changes to IN_PROGRESS
	@PutMapping("/start/{requestId}")
	public ResponseEntity<ExecutionResponseDTO> startService(@PathVariable Long requestId) {
	    return ResponseEntity.status(HttpStatus.SC_OK).body(executionService.startService(requestId));
	}
	
	
	
	// Only IN_PROGRESS requests can be completed
	// Status changes to COMPLETED
	@PutMapping("/complete/{requestId}")
	public ResponseEntity<ExecutionResponseDTO> completeService(@PathVariable Long requestId) {
	    return ResponseEntity.status(HttpStatus.SC_OK).body(executionService.completeService(requestId));
	}

}
