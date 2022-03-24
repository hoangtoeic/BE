package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.dto.ProductDTO;
import com.cnpm.ecommerce.backend.app.entity.Category;
import com.cnpm.ecommerce.backend.app.entity.Product;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.BrandRepository;
import com.cnpm.ecommerce.backend.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService implements IProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Override
    public List<Product> findAll() {

        List<Product> products = productRepository.findAll();
        for(Product product : products) {
            product.setThumbnail(Base64Utils.encodeToString(product.getThumbnailArr()));
            product.setCategoryId(product.getCategory().getId());
        }

        return products;
    }

    @Override
    public Page<Product> findAllPageAndSort(Pageable pagingSort) {
        Page<Product> productPage =  productRepository.findAll(pagingSort);

        for(Product product : productPage.getContent()) {
            product.setThumbnail(Base64Utils.encodeToString(product.getThumbnailArr()));
            product.setCategoryId(product.getCategory().getId());
        }
        return  productPage;
    }

    @Override
    public Product findById(Long theId) throws ResourceNotFoundException {
        Optional<Product> product = productRepository.findById(theId);
        if(!product.isPresent()) {
            throw  new ResourceNotFoundException("Not found product with ID=" + theId);
        } else {
            product.get().setThumbnail(Base64Utils.encodeToString(product.get().getThumbnailArr()));
            product.get().setCategoryId(product.get().getCategory().getId());
            return product.get();
        }

  }

    @Override
    public MessageResponse createProduct(ProductDTO theProductDto) {

        Product theProduct = new Product();

        theProduct.setName(theProductDto.getName());
        theProduct.setBrand(theProductDto.getBrand());
        theProduct.setShortDescription(theProductDto.getShortDescription());
        theProduct.setDescription(theProductDto.getDescription());
        theProduct.setPrice(theProductDto.getPrice());
        theProduct.setThumbnailArr(Base64Utils.decodeFromString(theProductDto.getThumbnail()));
        theProduct.setUnitInStock(theProductDto.getUnitInStock());
        theProduct.setCategory(categoryService.findById(theProductDto.getCategoryId()));
        theProduct.setBrandEntity(brandService.findByName(theProductDto.getBrand()));
        theProduct.setCreatedDate(new Date());
        theProduct.setCreatedBy(theProductDto.getCreatedBy());

        productRepository.save(theProduct);

        return new MessageResponse("Create product successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateProduct(Long theId, ProductDTO theProductDto) throws ResourceNotFoundException {

        Optional<Product> theProduct = productRepository.findById(theId);

        if(!theProduct.isPresent()) {
            throw new ResourceNotFoundException("Not found product with ID=" + theId);
        } else {
            theProduct.get().setName(theProductDto.getName());
            theProduct.get().setBrand(theProductDto.getBrand());
            theProduct.get().setShortDescription(theProductDto.getShortDescription());
            theProduct.get().setDescription(theProductDto.getDescription());
            theProduct.get().setPrice(theProductDto.getPrice());
            theProduct.get().setThumbnailArr(Base64Utils.decodeFromString(theProductDto.getThumbnail()));
            theProduct.get().setUnitInStock(theProductDto.getUnitInStock());
            theProduct.get().setCategory(categoryService.findById(theProductDto.getCategoryId()));
            theProduct.get().setBrandEntity(brandService.findByName(theProductDto.getBrand()));
            theProduct.get().setModifiedDate(new Date());
            theProduct.get().setModifiedBy(theProductDto.getModifiedBy());

            productRepository.save(theProduct.get());
        }

        return new MessageResponse("Update product successfully!" , HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteProduct(Long theId) throws ResourceNotFoundException {

        Product theProduct = productRepository.findById(theId).orElseThrow(
                () -> new ResourceNotFoundException("Not found product with ID=" + theId));

        productRepository.delete(theProduct);

    }

    @Override
    public Page<Product> findByNameContaining(String productName, Pageable pagingSort) {

        Page<Product> productPage =  productRepository.findByNameContainingIgnoreCase(productName, pagingSort);

        for(Product product : productPage.getContent()) {
            product.setThumbnail(Base64Utils.encodeToString(product.getThumbnailArr()));
            product.setCategoryId(product.getCategory().getId());
        }
        return  productPage;
    }

    @Override
    public Long count() {
        return productRepository.count();
    }




    @Override
    public Long countProductsByCategoryId(Long theCategoryId) {

        Category category = categoryService.findById(theCategoryId);

        if(category == null){
            throw  new ResourceNotFoundException("Not found category with ID= " + theCategoryId);
        } else {
            return productRepository.countProductsByCategoryId(theCategoryId);
        }
    }

    @Override
    public Page<Product> findByNameContainingAndPriceAndBrandPageAndSort(String productName, BigDecimal priceGTE, BigDecimal priceLTE, String brand, Pageable pagingSort) {
        Page<Product> productPage =  productRepository.findByNameContainingIgnoreCaseAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandContainingIgnoreCase(productName, priceGTE, priceLTE, brand, pagingSort);

        for(Product product : productPage.getContent()) {
            product.setThumbnail(Base64Utils.encodeToString(product.getThumbnailArr()));
            product.setCategoryId(product.getCategory().getId());
        }
        return  productPage;
    }

    @Override
    public Page<Product> findByNameContainingAndCategoryIdAndPriceAndBrandPageSort(String productName, Long categoryId, BigDecimal priceGTE, BigDecimal priceLTE, String brand, Pageable pagingSort) {
        Category category = categoryService.findById(categoryId);

        if(category == null){
            throw  new ResourceNotFoundException("Not found category with ID= " + categoryId);
        } else {

            Page<Product> productPage =  productRepository.findByNameContainingIgnoreCaseAndCategoryIdAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandContainingIgnoreCase
                    (productName, categoryId, priceGTE, priceLTE, brand, pagingSort);

            for(Product product : productPage.getContent()) {
                product.setThumbnail(Base64Utils.encodeToString(product.getThumbnailArr()));
                product.setCategoryId(product.getCategory().getId());
            }
            return  productPage;
        }
    }
}
