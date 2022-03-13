package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CategoryDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {

    List<Category> findAll();

    Category findById(Long theId);

    MessageResponse createCategory(CategoryDTO theCategoryDto);

    MessageResponse updateCategory(Long theId, CategoryDTO theCategoryDto);

    void deleteCategory(Long theId);

    Page<Category> findAllPageAndSort(Pageable pagingSort);

    Page<Category> findByNameContaining(String categoryName, Pageable pagingSort);

    Boolean existsByName(String name);
}
