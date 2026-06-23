package com.abc.categoryservice.service;

import java.util.List;

import com.abc.categoryservice.dto.CategoryRequest;
import com.abc.categoryservice.dto.CategoryResponse;
import com.abc.categoryservice.dto.CategoryStatusDTO;

public interface CategoryService {

    CategoryResponse createCategory(
            CategoryRequest request);

    CategoryResponse getCategoryById(
            Long categoryId);

    List<CategoryResponse> getAllCategories();

    CategoryResponse updateCategory(
            Long categoryId,
            CategoryRequest request);

    void deleteCategory(Long categoryId);

    CategoryResponse updateCategoryStatus(
            Long categoryId,
            CategoryStatusDTO statusDTO);
}
