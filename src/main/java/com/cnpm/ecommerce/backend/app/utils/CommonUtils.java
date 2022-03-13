package com.cnpm.ecommerce.backend.app.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    public static Sort.Direction getSortDirection(String sort) {
        return sort.contains("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    public static Pageable sortItem(int page, int limit, String [] sort) {

        List<Sort.Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                Sort.Direction dire = CommonUtils.getSortDirection(_sort[1]);
                Sort.Order order = new Sort.Order(dire,_sort[0]);
                orders.add( order );
            }
        } else {
            // sort=[field, direction]
            Sort.Direction dire = CommonUtils.getSortDirection(sort[1]);
            Sort.Order order = new Sort.Order(dire, sort[0]);
            orders.add( order );
        }
        Pageable pagingSort = PageRequest.of(page, limit, Sort.by(orders));

        return pagingSort;
    }
}
