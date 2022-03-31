package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
   Page<Brand> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Boolean existsByName(String name);

    Optional<Brand> findByName(String brandName);
}
