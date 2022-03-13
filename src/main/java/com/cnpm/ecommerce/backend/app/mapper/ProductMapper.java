package com.cnpm.ecommerce.backend.app.mapper;

import com.cnpm.ecommerce.backend.app.dto.ProductDTO;
import com.cnpm.ecommerce.backend.app.entity.Product;

public class ProductMapper {

    public static Product mapToProduct(ProductDTO productDTO) {
        Product product = new Product();

        return product;
    }

    public static ProductDTO mapToProductDto(Product product) {
        ProductDTO productDTO = new ProductDTO();

        return productDTO;
    }
}
