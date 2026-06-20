package com.hsms.execution_service.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsms.execution_service.DTO.BookingResponseDTO;
import com.hsms.execution_service.DTO.ExecutionResponseDTO;
import com.hsms.execution_service.FeignClient.BookingFeignClient;
import com.hsms.execution_service.entity.Execution;
import com.hsms.execution_service.enums.BookingStatus;
import com.hsms.execution_service.exception.ExecutionNotFoundException;
import com.hsms.execution_service.exception.ServiceUnavailableException;
import com.hsms.execution_service.repository.ExecutionRepository;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Service
public class ExecutionServiceImpl implements ExecutionService{
	
	@Autowired
	private ExecutionRepository executionRepo;

	@Autowired
	private BookingFeignClient bookingFeignClient;
	
	
	// Technician can accept assigned jobs
	// Status updated to ACCEPTED
	@Override
	@CircuitBreaker(name = "executionService", fallbackMethod = "acceptBookingFallback")
	public ExecutionResponseDTO acceptBooking(Long requestId) {
		Execution exe = executionRepo.findByServiceRequestId(requestId).orElseGet(() -> {
			Execution ex = new Execution(); 
			ex.setServiceRequestId(requestId);
			ex.setStatus(BookingStatus.ASSIGNED);
			return ex;});
		BookingResponseDTO booking = bookingFeignClient.getBookingById(requestId);
		if (!booking.getStatus().equals(BookingStatus.ASSIGNED) ) {
			throw new ExecutionNotFoundException("Only ASSIGNED bookings can be Assigned the Service");
		}
		bookingFeignClient.updateBookingStatus(requestId, BookingStatus.ACCEPTED);
		exe.setStatus(BookingStatus.ACCEPTED);
		executionRepo.save(exe);
		return new ExecutionResponseDTO(requestId, BookingStatus.ACCEPTED, exe.getStartTime(), exe.getEndTime());
	}
	public ExecutionResponseDTO acceptBookingFallback(Long requestId, Exception ex) {
	    throw new ServiceUnavailableException("Booking Service is currently unavailable. Please try again later.");
	}

	
	
	// Technician can reject assigned jobs
	// Status updated to REJECTED
	@Override
	@CircuitBreaker(name = "executionService", fallbackMethod = "rejectBookingFallback")
	public ExecutionResponseDTO rejectBooking(Long requestId) {
		Execution exe = executionRepo.findByServiceRequestId(requestId).orElseThrow(() -> new ExecutionNotFoundException("Booking Id "+requestId+" Not Found"));
		BookingResponseDTO booking = bookingFeignClient.getBookingById(requestId);
		if (!booking.getStatus().equals(BookingStatus.ASSIGNED) ) {
			throw new ExecutionNotFoundException("Only ASSIGNED bookings can be rejected the Service");
		}
		bookingFeignClient.updateBookingStatus(requestId, BookingStatus.REJECTED);
		exe.setStatus(BookingStatus.REJECTED);
		executionRepo.save(exe);
		return new ExecutionResponseDTO(requestId, BookingStatus.REJECTED, exe.getStartTime(), exe.getEndTime());
	}
	public ExecutionResponseDTO rejectBookingFallback(Long requestId,Exception ex) {
	    throw new ServiceUnavailableException("Booking Service is currently unavailable. Please try again later.");
	}
	
	// Only ACCEPTED requests can be started
	// Status changes to IN_PROGRESS
	@Override
	@CircuitBreaker(name = "executionService", fallbackMethod = "startServiceFallback")
	public ExecutionResponseDTO startService(Long requestId) {
		Execution exe = executionRepo.findByServiceRequestId(requestId).orElseThrow(() -> new ExecutionNotFoundException("Booking Id "+requestId+" Not Found"));
		BookingResponseDTO booking = bookingFeignClient.getBookingById(requestId);
		if (!booking.getStatus().equals(BookingStatus.ACCEPTED) ) {
			throw new ExecutionNotFoundException("Only ACCEPTED bookings can be Start the Service");
		}
		bookingFeignClient.updateBookingStatus(requestId, BookingStatus.IN_PROGRESS);
		exe.setStatus(BookingStatus.IN_PROGRESS);
	    exe.setStartTime(LocalDateTime.now());
	    executionRepo.save(exe);
		return new ExecutionResponseDTO(requestId, BookingStatus.IN_PROGRESS, exe.getStartTime(), exe.getEndTime());
	}
	public ExecutionResponseDTO startServiceFallback(Long requestId, Exception ex) {
	    throw new ServiceUnavailableException("Booking Service is currently unavailable. Please try again later.");
	}
	
	// Only IN_PROGRESS requests can be completed
	// Status changes to COMPLETED
	@Override
	@CircuitBreaker(name = "executionService", fallbackMethod = "completeServiceFallback")
	public ExecutionResponseDTO completeService(Long requestId) {
		Execution exe = executionRepo.findByServiceRequestId(requestId).orElseThrow(() -> new ExecutionNotFoundException("Booking Id "+requestId+" Not Found"));
		BookingResponseDTO booking = bookingFeignClient.getBookingById(requestId);
		if (!booking.getStatus().equals(BookingStatus.IN_PROGRESS) ) {
			throw new ExecutionNotFoundException("Only IN_PROGRESS bookings can be Complete the Service");
		}
		bookingFeignClient.updateBookingStatus(requestId, BookingStatus.COMPLETED);
		exe.setStatus(BookingStatus.COMPLETED);
		exe.setEndTime(LocalDateTime.now());
		executionRepo.save(exe);
		return new ExecutionResponseDTO(requestId, BookingStatus.COMPLETED, exe.getStartTime(), exe.getEndTime());
	}
	public ExecutionResponseDTO completeServiceFallback(Long requestId, Exception ex) {
	    throw new ServiceUnavailableException("Booking Service is currently unavailable. Please try again later.");
	}
	
}
