package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.BrandDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Brand;
import com.cnpm.ecommerce.backend.app.service.IBrandService;
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
@RequestMapping("/api/brands")
@CrossOrigin
public class BrandAPI {

    @Autowired
    private IBrandService brandService;

    @GetMapping("")
    public ResponseEntity<List<Brand>> getAll(@RequestParam(name = "q", required = false) String brandName,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int limit,
                                                 @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Brand> brandPage;

            if(brandName == null) {
                brandPage = brandService.findAllPageAndSort(pagingSort);
            } else {
                brandPage = brandService.findByNameContaining(brandName, pagingSort);
            }


            return new ResponseEntity<>(brandPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> findById(@PathVariable("id") Long theId){
        Brand theBrand = brandService.findById(theId);

        return new ResponseEntity<>(theBrand, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createBrand(@Valid @RequestBody BrandDTO theBrandDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for create brand", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        if(brandService.existsByName(theBrandDto.getName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Brand name is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }

        MessageResponse messageResponse = brandService.createBrand(theBrandDto);
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateBrand(@PathVariable("id") Long theId,
                                                          @Valid @RequestBody BrandDTO theBrandDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for update brand", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = brandService.updateBrand(theId, theBrandDto);
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable("id") Long theId){

        brandService.deleteBrand(theId);
        return new ResponseEntity<>(new MessageResponse("Deleted successfully!", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }
}
