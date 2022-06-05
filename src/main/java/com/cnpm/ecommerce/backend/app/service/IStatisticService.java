package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IStatisticService {

    BigDecimal getAllRevenueByDay(String day) throws ParseException;
    BigDecimal getAllRevenueByMonth(String month);
    BigDecimal getAllRevenueByQuarter(String quarter);
    BigDecimal getAllRevenueByYear(String year);

    List<Map<String, Object>> getAllSoldProductByDay(String day);
    List<Map<String, Object>> getAllSoldProductByMonth(String month);
    List<Map<String, Object>> getAllSoldProductByQuarter(String quarter);
    List<Map<String, Object>> getAllSoldProductByYear(String year);

    Page<Cart> getAllCartByDay(String day, Pageable pagingSort);
    Page<Cart> getAllCartByMonth(String month, Pageable pagingSort);
    Page<Cart> getAllCartByQuarter(String quarter, Pageable pagingSort);
    Page<Cart> getAllCartByYear(String year, Pageable pagingSort);

    Long getTotalOrderByDay(String day);
    Long getTotalOrderByMonth(String month);
    Long getTotalOrderByQuarter(String quarter);
    Long getTotalOrderByYear(String year);

    Long getTotalOrderDetailByDay(String day);
    Long getTotalOrderDetailByMonth(String month);
    Long getTotalOrderDetailByQuarter(String quarter);
    Long getTotalOrderDetailByYear(String year);
    List<Map<String, Object>> getTotalProductSoldGroupByCategoryByMonthInYear(String year);
}
