package cap_stone_project.caregory_service.service;

import java.util.List;

import cap_stone_project.caregory_service.model.CategoryDTO;

public interface CategoryService {
	
	CategoryDTO createCategory(CategoryDTO cateDTO);
	
	CategoryDTO findCateById(Long categoryId);
	
	CategoryDTO updateCategory(Long categoryId, CategoryDTO cateDTO);
	
	String deleteCategory(Long categoryId);
	
	List<CategoryDTO> showAllCategories();

}
