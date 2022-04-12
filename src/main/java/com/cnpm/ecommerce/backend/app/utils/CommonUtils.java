package com.cnpm.ecommerce.backend.app.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        String url = "https://free.currconv.com/api/v7/convert?q=USD_VND&compact=ultra&apiKey=41700239ba29f3a2666f";

        RestTemplate restTemplate = new RestTemplate();

        Object object = restTemplate.getForObject(url, Object.class);

        String ex = object.toString();

        String[] arr = ex.split("=");

        return Double.parseDouble(arr[1].substring(0, arr[1].length() - 1));
    }

}
