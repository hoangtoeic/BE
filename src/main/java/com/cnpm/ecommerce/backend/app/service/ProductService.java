package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.dto.ProductDTO;
import com.cnpm.ecommerce.backend.app.entity.*;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.CartItemRepository;
import com.cnpm.ecommerce.backend.app.repository.FeedbackRepository;
import com.cnpm.ecommerce.backend.app.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Transactional
public class ProductService implements IProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Override
    public List<Product> findAll() {

        List<Product> products = productRepository.findAll();

        for(Product product : products) {
            product.setThumbnail(Base64Utils.encodeToString(product.getThumbnailArr()));
            product.setCategoryIds(product.getCategory().getId());
        }

        return products;
    }

    @Override
    public Page<Product> findAllPageAndSort(Pageable pagingSort) {
        Page<Product> productPage =  productRepository.findAll(pagingSort);

        for(Product product : productPage.getContent()) {
            product.setThumbnail(Base64Utils.encodeToString(product.getThumbnailArr()));
            product.setCategoryIds(product.getCategory().getId());
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
            product.get().setCategoryIds(product.get().getCategory().getId());

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
        if(theProductDto.getThumbnail() != null) {
            theProduct.setThumbnailArr(Base64Utils.decodeFromString(theProductDto.getThumbnail()));
        } else {
            theProduct.setThumbnailArr(new byte[0]);
        }
        theProduct.setUnitInStock(theProductDto.getUnitInStock());
        theProduct.setCategory(categoryService.findById(theProductDto.getCategoryId()));
        theProduct.setBrandEntity(brandService.findByName(theProductDto.getBrand()));
        theProduct.setCreatedDate(new Date());
        theProduct.setCreatedBy(theProductDto.getCreatedBy());


        if(theProductDto.getDiscount() == null) {
            theProduct.setDiscount(0);
        } else {
            theProduct.setDiscount(theProductDto.getDiscount());
        }

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
            if(theProductDto.getThumbnail() != null) {
                theProduct.get().setThumbnailArr(Base64Utils.decodeFromString(theProductDto.getThumbnail()));
            } else {
                theProduct.get().setThumbnailArr(new byte[0]);
            }
            theProduct.get().setUnitInStock(theProductDto.getUnitInStock());
            theProduct.get().setCategory(categoryService.findById(theProductDto.getCategoryId()));
            theProduct.get().setBrandEntity(brandService.findByName(theProductDto.getBrand()));
            theProduct.get().setModifiedDate(new Date());
            theProduct.get().setModifiedBy(theProductDto.getModifiedBy());

            if(theProductDto.getDiscount() == null) {
                theProduct.get().setDiscount(0);
            } else {
                theProduct.get().setDiscount(theProductDto.getDiscount());
            }

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
            product.setCategoryIds(product.getCategory().getId());
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
            product.setCategoryIds(product.getCategory().getId());

        }
        return  productPage;
    }

    @Override
    public Page<Product> findByNameContainingAndCategoryIdAndPriceAndBrandPageSort(String productName, Long categoryId, BigDecimal priceGTE, BigDecimal priceLTE, String brand, Pageable pagingSort) {
        Category category = categoryService.findById(categoryId);

        if(category == null){
            throw  new ResourceNotFoundException("Not found category with ID= " + categoryId);
        } else {
            try{
                Page<Product> productPage =  productRepository.findByNameContainingIgnoreCaseAndCategoryIdAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandContainingIgnoreCase
                        (productName, categoryId, priceGTE, priceLTE, brand, pagingSort);

                for(Product product : productPage.getContent()) {
                    product.setThumbnail(Base64Utils.encodeToString(product.getThumbnailArr()));
                    product.setCategoryIds(categoryId);

                }
                return  productPage;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }
    }

    @Override
    public Page<Product> recommendSystem(Long userID, Pageable pagingSort)  {

        /* find the most recent rating in feedback table by userID
        if not exist, send the 10 most appearing products in cart table* */
        Integer productID = -1;
        List<Long> list2 = new ArrayList<Long>();
        okLetDoIt: try{
            System.out.println("userID" + userID);
            List<Feedback> productsByUserID =  feedbackRepository.findProductBuyMostRecent(userID);
            if (productsByUserID.size() == 0) break okLetDoIt;
            Feedback feedback = productsByUserID.get(productsByUserID.size() - 1);
            productID = feedback.getProduct().getId().intValue();
            System.out.println("productID" + productID);

            String productListTypeString = "";
            RestTemplate restTemplate = new RestTemplate();

            List<Long> exceptProductID = new ArrayList<>();
            exceptProductID = feedbackRepository.findProductIDRatingSmaller4(userID);
            System.out.println("exceptProductID" + exceptProductID);

            recommendRequestObject requestObject = new recommendRequestObject();
            requestObject.setId(Math.toIntExact(userID));
            requestObject.setExceptProductID(exceptProductID);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<recommendRequestObject> entity = new HttpEntity<recommendRequestObject>(requestObject,headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        "https://recommendsystem2023.herokuapp.com/recommendProduct", HttpMethod.POST, entity, String.class);

                ObjectMapper objectMapper = new ObjectMapper();
                recommendResponseObject responseObject = objectMapper.readValue(response.getBody(), recommendResponseObject.class);
                productListTypeString = responseObject.getProductList();
                System.out.println("recommendResponseObject:" + productListTypeString);
            }
            catch (RuntimeException e) {
                System.out.println("faild for query recommend system" + e);
                e.printStackTrace();
                return null;
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // Convert productListTypeString to List<Long>

            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(productListTypeString);
            System.out.println("matchermatcher:" + matcher);

            while (matcher.find()) {
                list2.add(Long.parseLong(matcher.group())); // Add the value to the list
            }
            System.out.println(list2);

        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        System.out.println("productID2" + productID);

        /* if not exist, send the 10 most appearing products in cart table* */
        if(productID == -1) {
           list2 = cartItemRepository.findElementsMostAppear();
            System.out.println("list233" + list2);
        }

        System.out.println("list2list2" + list2);

        Page<Product> productPage =  productRepository.findProductBylistID(list2, pagingSort);
        System.out.println("productPageproductPage" + productPage);

        for(Product product : productPage.getContent()) {
            product.setThumbnail(Base64Utils.encodeToString(product.getThumbnailArr()));
        }
        return productPage;
    }
}


