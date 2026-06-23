package com.abc.categoryservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.abc.categoryservice.dto.CategoryRequest;
import com.abc.categoryservice.dto.CategoryResponse;
import com.abc.categoryservice.dto.CategoryStatusDTO;
import com.abc.categoryservice.entity.ServiceCategory;
import com.abc.categoryservice.exception.CategoryNotFoundException;
import com.abc.categoryservice.exception.DuplicateCategoryException;
import com.abc.categoryservice.repository.CategoryRepository;

class CategoryServiceImplTest {

    private CategoryRepository repository;
    private ModelMapper mapper;
    private CategoryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(CategoryRepository.class);
        mapper = new ModelMapper();
        service = new CategoryServiceImpl(repository, mapper);
    }

    @Test
    void testCreateCategory() {
        CategoryRequest request = new CategoryRequest("Plumbing", "Pipe Repairs", 150.0, true);

        when(repository.existsByCategoryName("Plumbing")).thenReturn(false);

        ServiceCategory category = new ServiceCategory(1L, "Plumbing", "Pipe Repairs", 150.0, true);
        when(repository.save(any(ServiceCategory.class))).thenReturn(category);

        CategoryResponse response = service.createCategory(request);

        assertEquals("Plumbing", response.getCategoryName());
        assertEquals("Pipe Repairs", response.getDescription());
        assertEquals(150.0, response.getBasePrice());
        assertEquals(true, response.getActive());
        assertEquals(1L, response.getCategoryId());
    }

    @Test
    void testCreateDuplicateCategory() {
        CategoryRequest request = new CategoryRequest("Plumbing", "Pipe Repairs", 150.0, true);

        when(repository.existsByCategoryName("Plumbing")).thenReturn(true);

        assertThrows(DuplicateCategoryException.class, () -> service.createCategory(request));
    }

    @Test
    void testGetCategoryById_Success() {
        ServiceCategory category = new ServiceCategory(1L, "Plumbing", "Pipe Repairs", 150.0, true);

        when(repository.findByCategoryIdAndActiveTrue(1L)).thenReturn(Optional.of(category));

        CategoryResponse response = service.getCategoryById(1L);

        assertEquals(1L, response.getCategoryId());
        assertEquals("Plumbing", response.getCategoryName());
        assertEquals(150.0, response.getBasePrice());
        assertEquals(true, response.getActive());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(repository.findByCategoryIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.getCategoryById(1L));
    }

    @Test
    void testGetAllCategories() {
        ServiceCategory category1 = new ServiceCategory(1L, "Plumbing", "Repairs", 120.0, true);
        ServiceCategory category2 = new ServiceCategory(2L, "Electrical", "Wiring", 180.0, true);

        when(repository.findByActiveTrue()).thenReturn(Arrays.asList(category1, category2));

        List<CategoryResponse> list = service.getAllCategories();

        assertEquals(2, list.size());
        assertEquals("Plumbing", list.get(0).getCategoryName());
        assertEquals(120.0, list.get(0).getBasePrice());
        assertEquals("Electrical", list.get(1).getCategoryName());
        assertEquals(180.0, list.get(1).getBasePrice());
    }

    @Test
    void testUpdateCategory_Success() {
        CategoryRequest request = new CategoryRequest("Plumbing Work", "Pipe Repairs Updated", 160.0, true);
        ServiceCategory category = new ServiceCategory(1L, "Plumbing", "Pipe Repairs", 150.0, true);

        when(repository.findByCategoryIdAndActiveTrue(1L)).thenReturn(Optional.of(category));
        when(repository.findByCategoryName("Plumbing Work")).thenReturn(Optional.empty());

        ServiceCategory updatedCategory = new ServiceCategory(1L, "Plumbing Work", "Pipe Repairs Updated", 160.0, true);
        when(repository.save(any(ServiceCategory.class))).thenReturn(updatedCategory);

        CategoryResponse response = service.updateCategory(1L, request);

        assertEquals("Plumbing Work", response.getCategoryName());
        assertEquals("Pipe Repairs Updated", response.getDescription());
        assertEquals(160.0, response.getBasePrice());
    }

    @Test
    void testUpdateCategory_DuplicateName() {
        CategoryRequest request = new CategoryRequest("Electrical", "Wiring", 150.0, true);
        ServiceCategory category = new ServiceCategory(1L, "Plumbing", "Repairs", 120.0, true);
        ServiceCategory conflictCategory = new ServiceCategory(2L, "Electrical", "Wiring", 150.0, true);

        when(repository.findByCategoryIdAndActiveTrue(1L)).thenReturn(Optional.of(category));
        when(repository.findByCategoryName("Electrical")).thenReturn(Optional.of(conflictCategory));

        assertThrows(DuplicateCategoryException.class, () -> service.updateCategory(1L, request));
    }

    @Test
    void testUpdateCategory_NotFound() {
        CategoryRequest request = new CategoryRequest("Plumbing", "Repairs", 120.0, true);

        when(repository.findByCategoryIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.updateCategory(1L, request));
    }

    @Test
    void testDeleteCategory_Success() {
        ServiceCategory category = new ServiceCategory(1L, "Plumbing", "Repairs", 120.0, true);

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.save(any(ServiceCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.deleteCategory(1L);

        assertFalse(category.getActive());
        verify(repository, times(1)).save(category);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.deleteCategory(1L));
    }

    @Test
    void testUpdateCategoryStatus_Success() {
        ServiceCategory category = new ServiceCategory(1L, "Plumbing", "Repairs", 120.0, true);
        CategoryStatusDTO statusDTO = new CategoryStatusDTO(false);

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.save(any(ServiceCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponse response = service.updateCategoryStatus(1L, statusDTO);

        assertFalse(response.getActive());
        verify(repository, times(1)).save(category);
    }

    @Test
    void testUpdateCategoryStatus_NotFound() {
        CategoryStatusDTO statusDTO = new CategoryStatusDTO(false);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.updateCategoryStatus(1L, statusDTO));
    }
}