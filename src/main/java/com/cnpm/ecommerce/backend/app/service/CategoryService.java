package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.CategoryDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Category;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService implements ICategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        List<Category> categories = categoryRepository.findAll();
        for(Category category : categories) {
            category.setThumbnail(Base64Utils.encodeToString(category.getThumbnailArr()));
        }

        return categories;
    }

    @Override
    public Category findById(Long theId) throws ResourceNotFoundException {
        Optional<Category> category = categoryRepository.findById(theId);
        if(!category.isPresent()) {
            throw  new ResourceNotFoundException("Not found category with ID=" + theId);
        } else {
            category.get().setThumbnail(Base64Utils.encodeToString(category.get().getThumbnailArr()));
            return category.get();
        }
    }

    @Override
    public MessageResponse createCategory(CategoryDTO theCategoryDto) {

        Category theCategory = new Category();

        theCategory.setCreatedDate(new Date());
        theCategory.setCreatedBy(theCategoryDto.getCreatedBy());
        theCategory.setName(theCategoryDto.getName());
        theCategory.setDescription(theCategoryDto.getDescription());
        theCategory.setThumbnailArr(Base64Utils.decodeFromString(theCategoryDto.getThumbnail()));

        categoryRepository.save(theCategory);

        return new MessageResponse("Create category successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateCategory(Long theId, CategoryDTO theCategoryDto) {
        Optional<Category> theCategory = categoryRepository.findById(theId);

        if(!theCategory.isPresent()) {
            throw new ResourceNotFoundException("Not found category with ID=" + theId);
        } else {

            if(!theCategory.get().getName().equals(theCategoryDto.getName())){
                if(categoryRepository.existsByName(theCategoryDto.getName())){
                    return new MessageResponse("Error: Category name is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
                }
            }

            theCategory.get().setModifiedDate(new Date());
            theCategory.get().setModifiedBy(theCategoryDto.getModifiedBy());
            theCategory.get().setName(theCategoryDto.getName());
            theCategory.get().setDescription(theCategoryDto.getDescription());
            theCategory.get().setThumbnailArr(Base64Utils.decodeFromString(theCategoryDto.getThumbnail()));

            categoryRepository.save(theCategory.get());
        }

        return new MessageResponse("Updated category successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteCategory(Long theId) {
        Category theCategory = categoryRepository.findById(theId).orElseThrow(
                () -> new ResourceNotFoundException("Not found category with ID=" + theId));

        categoryRepository.delete(theCategory);
    }

    @Override
    public Page<Category> findAllPageAndSort(Pageable pagingSort) {
        Page<Category> categoryPage =  categoryRepository.findAll(pagingSort);

        for(Category category : categoryPage.getContent()) {
            category.setThumbnail(Base64Utils.encodeToString(category.getThumbnailArr()));
        }
        return  categoryPage;
    }

    @Override
    public Page<Category> findByNameContaining(String categoryName, Pageable pagingSort) {
        Page<Category> categoryPage =  categoryRepository.findByNameContainingIgnoreCase(categoryName, pagingSort);

        for(Category category : categoryPage.getContent()) {
            category.setThumbnail(Base64Utils.encodeToString(category.getThumbnailArr()));
        }
        return  categoryPage;
    }

    @Override
    public Boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
