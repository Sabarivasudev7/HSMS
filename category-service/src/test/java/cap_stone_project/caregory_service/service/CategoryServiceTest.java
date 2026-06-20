package cap_stone_project.caregory_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import cap_stone_project.caregory_service.entity.Category;
import cap_stone_project.caregory_service.exception.CategoryNotFoundException;
import cap_stone_project.caregory_service.exception.DuplicateCategoryException;
import cap_stone_project.caregory_service.model.CategoryDTO;
import cap_stone_project.caregory_service.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
	
	@InjectMocks
	private CategoryServiceImpl cateService;
	
	@Mock
	private CategoryRepository cateRepo;
	
	@Mock
	private ModelMapper modelMapper;
	
	
	
	@Test
	void testCrateCategory() {
		CategoryDTO dto = new CategoryDTO("Plumbing", "Pipe Service", 500, true);
		Category cate = new Category(null, "Plumbing", "Pipe Service", 500, true);
		
		when(cateRepo.findByCategoryName("Plumbing")).thenReturn(Optional.empty());
		when(modelMapper.map(dto, Category.class)).thenReturn(cate);
		when(cateRepo.save(cate)).thenReturn(cate);
		when(modelMapper.map(cate, CategoryDTO.class)).thenReturn(dto);
		
		CategoryDTO result = cateService.createCategory(dto);
		
		assertEquals("Plumbing", result.getCategoryName());
		
	}
	
	
	
	@Test
    void testCreateCategoryWithDuplicateException() {

        Category category = new Category(null, "Plumbing", "Pipe Services", 500, true);

        CategoryDTO dto = new CategoryDTO("Plumbing", "Pipe Services", 500, true);

        when(cateRepo.findByCategoryName("Plumbing")).thenReturn(Optional.of(category));
        
        assertThrows(DuplicateCategoryException.class,() -> cateService.createCategory(dto));
    }

	
	
	
    @Test
    void testFindCategoryById() {

        Category category = new Category(null, "Plumbing",  "Pipe Services", 500, true);

        CategoryDTO dto = new CategoryDTO( "Plumbing", "Pipe Services", 500, true);

        when(cateRepo.findById(1L)).thenReturn(Optional.of(category));

        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(dto);

        CategoryDTO result =cateService.findCateById(1L);

        assertEquals("Plumbing", result.getCategoryName());
    }

    
    
    
    @Test
    void testFindCategoryByIdWithException() {

        when(cateRepo.findById(100L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
        () -> cateService.findCateById(100L));
    }

    
    
    
    @Test
    void testDeleteCategory() {

        Category category = new Category(null,"Plumbing", "Pipe Services", 500, true);

        when(cateRepo.findById(1L)).thenReturn(Optional.of(category));

        String result = cateService.deleteCategory(1L);

        verify(cateRepo, times(1)).save(category);

        assertEquals("Category deleted successfully", result);
    }

    
    
    
    @Test
    void testShowAllCategories() {

        Category category = new Category(null, "Plumbing", "Pipe Services", 500, true);

        CategoryDTO dto = new CategoryDTO("Plumbing","Pipe Services", 500, true);

        when(cateRepo.findByIsActiveTrue()).thenReturn(Arrays.asList(category));

        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(dto);

        List<CategoryDTO> result = cateService.showAllCategories();

        assertEquals(1, result.size());
    }
	

}
