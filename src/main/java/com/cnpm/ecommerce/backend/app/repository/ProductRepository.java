package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p from Product p WHERE p.category.id=?1")
    List<Product> findProductsByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE %?1%"
            + " OR lower(p.shortDescription) LIKE %?1% OR lower(p.description) LIKE %?1% OR lower(p.brand) LIKE %?1%" )
    List<Product> search( String key);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT count(p) from Product p WHERE p.category.id=?1")
    Long countProductsByCategoryId(Long categoryId);

    Page<Product> findByNameContainingIgnoreCaseAndCategoryIdAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandContainingIgnoreCase(
            String productName, Long categoryId, BigDecimal priceGTE, BigDecimal priceLTE, String brand, Pageable pagingSort);

    Page<Product> findByNameContainingIgnoreCaseAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandContainingIgnoreCase(
            String productName, BigDecimal priceGTE, BigDecimal priceLTE, String brand, Pageable pagingSort);

    @Query(value = "SELECT p FROM Product p WHERE p.id IN :listID")
    List<Product> findProductBylistID(Collection<Long> listID);
}





