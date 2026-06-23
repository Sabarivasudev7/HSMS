package com.abc.feedbackservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.abc.feedbackservice.model.ServiceRequestResponse;

@FeignClient(name = "service-request-service", fallback = ServiceRequestClient.ServiceRequestClientFallback.class)
public interface ServiceRequestClient {

	@GetMapping("/api/service-requests/{id}")
	ServiceRequestResponse getServiceRequest(
		@PathVariable Long id,
		@RequestHeader("X-Role") String role
	);

	@Component
	class ServiceRequestClientFallback implements ServiceRequestClient {
		@Override
		public ServiceRequestResponse getServiceRequest(Long id, String role) {
			return null;
		}
	}
}

