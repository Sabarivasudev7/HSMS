package com.abc.categoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.categoryservice.entity.ServiceCategory;

public interface CategoryRepository
        extends JpaRepository<ServiceCategory, Long> {

    Optional<ServiceCategory> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);

    List<ServiceCategory> findByActiveTrue();

    Optional<ServiceCategory> findByCategoryIdAndActiveTrue(Long categoryId);

    // Backward compatibility default methods for legacy callers
    default Optional<ServiceCategory> findByName(String name) {
        return findByCategoryName(name);
    }

    default boolean existsByName(String name) {
        return existsByCategoryName(name);
    }

    default Optional<ServiceCategory> findByIdAndActiveTrue(Long id) {
        return findByCategoryIdAndActiveTrue(id);
    }
}