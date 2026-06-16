package cap_stone_project.booking_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cap_stone_project.booking_service.model.CategoryResponseDTO;

@FeignClient(name = "CATEGORY-SERVICE")
public interface CategoryFeignClient {
	
	
	@GetMapping("/api/category/{categoryId}")
	CategoryResponseDTO getCategoryById(@PathVariable Long categoryId);

}
