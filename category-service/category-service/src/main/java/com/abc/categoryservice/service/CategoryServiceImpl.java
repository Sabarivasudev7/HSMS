package com.abc.categoryservice.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.abc.categoryservice.dto.CategoryRequest;
import com.abc.categoryservice.dto.CategoryResponse;
import com.abc.categoryservice.dto.CategoryStatusDTO;
import com.abc.categoryservice.entity.ServiceCategory;
import com.abc.categoryservice.exception.CategoryNotFoundException;
import com.abc.categoryservice.exception.DuplicateCategoryException;
import com.abc.categoryservice.repository.CategoryRepository;


@Service
public class CategoryServiceImpl
        implements CategoryService {

    private final CategoryRepository repository;
    private final ModelMapper mapper;

    public CategoryServiceImpl(
            CategoryRepository repository,
            ModelMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CategoryResponse createCategory(
            CategoryRequest request) {

        if(repository.existsByCategoryName(
                request.getCategoryName())) {

            throw new DuplicateCategoryException(
                    "Category already exists with name : "
                            + request.getCategoryName());
        }

        ServiceCategory category =
                mapper.map(
                        request,
                        ServiceCategory.class);

        ServiceCategory saved =
                repository.save(category);

        return mapper.map(
                saved,
                CategoryResponse.class);
    }

    @Override
    public CategoryResponse getCategoryById(
            Long categoryId) {

        ServiceCategory category =
        		repository.findByCategoryIdAndActiveTrue(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id : "
                                        + categoryId));

        return mapper.map(
                category,
                CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

    	return repository.findByActiveTrue()
                .stream()
                .map(category ->
                        mapper.map(
                                category,
                                CategoryResponse.class))
                .toList();
    }

    @Override
    public CategoryResponse updateCategory(
            Long categoryId,
            CategoryRequest request) {

        ServiceCategory category =
                repository.findByCategoryIdAndActiveTrue(
                        categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id : "
                                        + categoryId));

        Optional<ServiceCategory> existing =
                repository.findByCategoryName(
                        request.getCategoryName());

        if(existing.isPresent()
                &&
                !existing.get().getCategoryId()
                        .equals(categoryId)) {

            throw new DuplicateCategoryException(
                    "Category already exists with name : "
                            + request.getCategoryName());
        }

        category.setCategoryName(
                request.getCategoryName());

        category.setDescription(
                request.getDescription());

        category.setBasePrice(
                request.getBasePrice());

        category.setActive(
                request.getActive());

        ServiceCategory updated =
                repository.save(category);

        return mapper.map(
                updated,
                CategoryResponse.class);
    }
    
    

    @Override
    public void deleteCategory(Long categoryId) {

        ServiceCategory category =
                repository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id : "
                                        + categoryId));

        category.setActive(false);
        repository.save(category);
    }

    @Override
    public CategoryResponse updateCategoryStatus(
            Long categoryId,
            CategoryStatusDTO statusDTO) {

        ServiceCategory category =
                repository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id : "
                                        + categoryId));

        category.setActive(statusDTO.getActive());
        ServiceCategory updated = repository.save(category);

        return mapper.map(
                updated,
                CategoryResponse.class);
    }
}