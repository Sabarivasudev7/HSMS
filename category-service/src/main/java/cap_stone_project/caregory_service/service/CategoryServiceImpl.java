package cap_stone_project.caregory_service.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cap_stone_project.caregory_service.entity.Category;
import cap_stone_project.caregory_service.exception.CategoryNotFoundException;
import cap_stone_project.caregory_service.exception.DuplicateCategoryException;
import cap_stone_project.caregory_service.model.CategoryDTO;
import cap_stone_project.caregory_service.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	
	@Autowired
	private CategoryRepository cateRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	// Create Category
	@Override
	public CategoryDTO createCategory(CategoryDTO cateDTO) {
		Optional<Category> cate = cateRepo.findByCategoryName(cateDTO.getCategoryName());
		if (cate.isPresent()) {
			throw new DuplicateCategoryException("Category '"+cateDTO.getCategoryName()+"' already exists.");
		}
		Category category = modelMapper.map(cateDTO, Category.class);
		category.setActive(true);
		Category saveCategory = cateRepo.save(category);
		return modelMapper.map(saveCategory, CategoryDTO.class);
	}

	
	
	// Find Category By Id
	@Override
	public CategoryDTO findCateById(Long categoryId) {
		Category cate = cateRepo.findById(categoryId)
			.orElseThrow(() -> new CategoryNotFoundException
			("Category Id "+categoryId+" not found"));
				
		return modelMapper.map(cate, CategoryDTO.class);
	}

	
	
	// Update Category
	@Override
	public CategoryDTO updateCategory(Long categoryId, CategoryDTO cateDTO) {
		Optional<Category> cateName = cateRepo.findByCategoryName(cateDTO.getCategoryName());
		if (cateName.isPresent() && cateName.get().getCategoryId() != categoryId) {
			throw new DuplicateCategoryException("Category '"+cateDTO.getCategoryName()+"' already exists.");
		}
		
		Category cate = cateRepo.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException
				("Category Id "+categoryId+" not found."));
		
		cate.setCategoryName(cateDTO.getCategoryName());
		cate.setDescription(cateDTO.getDescription());
		cate.setBasePrice(cateDTO.getBasePrice());
		
		Category newCate = cateRepo.save(cate);
		return modelMapper.map(newCate, CategoryDTO.class);
	}

	
	
	// Delete Category
	@Override
	public String deleteCategory(Long categoryId) {
		Category cate = cateRepo.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException
				("Category Id "+categoryId+" not found"));
		
		cate.setActive(false);  // setActive(boolean isActive)
		cateRepo.save(cate);
		return "Category deleted successfully";
	}

	
	
	
	// Show ALL Categories
	@Override
	public List<CategoryDTO> showAllCategories() {
		List<Category> cates = cateRepo.findByIsActiveTrue();
		return cates.stream().map(c -> modelMapper
				.map(c, CategoryDTO.class))
				.collect(Collectors.toList());

	}

}
