package com.hsms.analytics.clients;

import java.util.List;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.hsms.analytics.model.CategoryDto;

@FeignClient(name = "CATEGORY-SERVICE", contextId = "categoryClient")
public interface CategoryClient {
	@GetMapping("/api/category/all")
	List<CategoryDto> getAllCategories();
}