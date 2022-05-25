package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.entity.Product;

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
}
