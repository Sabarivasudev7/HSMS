package cap_stone_project.booking_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cap_stone_project.booking_service.model.CustomerResponseDTO;

@FeignClient(name = "USER-SERVICE")
public interface CustomerFeignClient {
	
	
	@GetMapping("/api/customers/{customerId}")
	CustomerResponseDTO findCustomerById(@PathVariable Long customerId);

}

