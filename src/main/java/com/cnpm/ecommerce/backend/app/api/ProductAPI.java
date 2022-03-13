package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.dto.ProductDTO;
import com.cnpm.ecommerce.backend.app.entity.Product;
import com.cnpm.ecommerce.backend.app.service.IProductService;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductAPI {

    @Autowired
    private IProductService productService;

    @GetMapping("")
    public ResponseEntity<List<Product>> findAll( @RequestParam(name = "q", required = false) String productName,
                                                  @RequestParam(name = "categoryId", required = false) Long categoryId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int limit,
                                                  @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Product> productPage = null;

            if(productName == null && categoryId == null) {
                productPage = productService.findAllPageAndSort(pagingSort);
            } else {
                if(categoryId == null) {
                    productPage = productService.findByNameContaining(productName, pagingSort);
                } else if(productName == null) {
                    productPage = productService.findByCategoryIdPageAndSort(categoryId, pagingSort);
                }

            }

            return new ResponseEntity<>(productPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Long theId){

        Product theProduct = productService.findById(theId);
        return new ResponseEntity<>(theProduct, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createProduct(@Valid @RequestBody ProductDTO theProductDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for create product", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = productService.createProduct(theProductDto);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateProduct(@PathVariable("id") Long theId,
                                                         @Valid @RequestBody ProductDTO theProductDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for update product", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = productService.updateProduct(theId, theProductDto);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long theId){

        productService.deleteProduct(theId);
        return new ResponseEntity<>(new MessageResponse("Delete successfully!", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<?> count(){
        return new ResponseEntity<>(productService.count(), HttpStatus.OK);
    }

}
