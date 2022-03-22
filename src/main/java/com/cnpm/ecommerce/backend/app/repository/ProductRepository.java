package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p from Product p WHERE p.category.id=?1")
    List<Product> findProductsByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE %?1%"
            + " OR lower(p.shortDescription) LIKE %?1% OR lower(p.description) LIKE %?1% OR lower(p.brand) LIKE %?1%" )
    List<Product> search( String key);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategoryId(Long id, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndCategoryId(String productName, Long categoryId, Pageable pagingSort);

    @Query("SELECT count(p) from Product p WHERE p.category.id=?1")
    Long countProductsByCategoryId(Long categoryId);
}
