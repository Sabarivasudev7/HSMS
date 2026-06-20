package cap_stone_project.caregory_service.controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cap_stone_project.caregory_service.model.CategoryDTO;
import cap_stone_project.caregory_service.service.CategoryService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/category")
public class CategoryController {
	
	@Autowired
	private CategoryService cateService;
	
	
	@PostMapping("/save")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO cateDTO) {
		return ResponseEntity.status(HttpStatus.SC_CREATED).body(cateService.createCategory(cateDTO));
	}
	
	
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDTO> findCategoryById(@PathVariable Long categoryId) {
		return ResponseEntity.status(HttpStatus.SC_OK).body(cateService.findCateById(categoryId));
	}
	
	
	
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryDTO cateDTO) {
		return ResponseEntity.status(HttpStatus.SC_OK).body(cateService.updateCategory(categoryId, cateDTO));
	}

	
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<String> deleteCateegory(@PathVariable Long categoryId) {
		return ResponseEntity.status(HttpStatus.SC_OK).body(cateService.deleteCategory(categoryId));
	}
	
	
	
	@GetMapping("/all")
	public ResponseEntity<List<CategoryDTO>> showAllCategories() {
		return ResponseEntity.status(HttpStatus.SC_OK).body(cateService.showAllCategories());
	}
	
	
	
}
