package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.BrandDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBrandService {

    List<Brand> findAll();

    Brand findById(Long theId);

    MessageResponse createBrand(BrandDTO theBrandDto);

    MessageResponse updateBrand(Long theId, BrandDTO theBrandDto);

    MessageResponse deleteBrand(Long theId);

    Page<Brand> findAllPageAndSort(Pageable pagingSort);

    Page<Brand> findByNameContaining(String brandName, Pageable pagingSort);

    Boolean existsByName(String name);

    Brand findByName(String brand);
}
