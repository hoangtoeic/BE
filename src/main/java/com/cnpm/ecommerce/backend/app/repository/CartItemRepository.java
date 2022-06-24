package com.cnpm.ecommerce.backend.app.repository;

import com.cnpm.ecommerce.backend.app.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT c from CartItem c WHERE c.cart.id=?1")
    List<CartItem> findCartItemByCartID(Long cartId);

    Page<CartItem> findById(Long id, Pageable pageable);

    Page<CartItem> findByCartId(Long cartId, Pageable pageable);

    @Query("SELECT c.product.id AS productId, c.product.name AS productName, SUM(c.quantity) AS quantity, SUM(c.quantity) * c.product.price AS totalPrice from CartItem c WHERE c.createdDate>=?1 AND c.createdDate<?2 GROUP BY c.product.id, c.product.name, c.product.price ORDER BY SUM(c.quantity) DESC")
    List<Map<String, Object>> getAllSoldProductByDay(Timestamp date, Timestamp dateEndDate, PageRequest of);


    @Query("select c.product.id from CartItem c GROUP BY c.product.id ORDER BY COUNT(c.product.id) DESC")
    List<Long> findElementsMostAppear();

    @Query("SELECT count(c) from CartItem c WHERE c.createdDate>=?1 AND c.createdDate<?2")
    Long getTotalOrderDetailByDay(Timestamp date, Timestamp dateEndDate);

    @Query("SELECT c.product.category.id AS categoryId, c.product.category.name AS categoryName, SUM(c.quantity) AS quantity from CartItem c WHERE c.createdDate>=?1 AND c.createdDate<?2 GROUP BY c.product.category.id, c.product.category.name ORDER BY c.product.category.id ASC")
    List<Map<String, Object>> getTotalProductSoldGroupByCategory(Timestamp date, Timestamp dateEndDate);
}
