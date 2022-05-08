package com.cnpm.ecommerce.backend.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static String getBaseURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url = new StringBuffer();

        url.append(scheme).append("://").append(serverName);

        if(serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if(url.toString().endsWith("/")) {
            url.append("/");
        }

        return url.toString();
    }

    public static Double exchangeCurrency(){
        String url = "https://v6.exchangerate-api.com/v6/675b2f4248bd548f7b3f133f/latest/USD";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> object = restTemplate.getForObject(url, HashMap.class);

        Map<String, Object> conversionRates = (Map<String, Object>) object.get("conversion_rates");

        Object usd = conversionRates.get("VND");

        return Double.parseDouble(usd.toString());
    }

}
