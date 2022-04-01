package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.BrandDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Brand;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BrandService implements IBrandService{

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> findAll() {
        List<Brand> brands = brandRepository.findAll();
        for(Brand brand : brands) {
            brand.setThumbnail(Base64Utils.encodeToString(brand.getThumbnailArr()));
        }

        return brands;
    }

    @Override
    public Brand findById(Long theId) throws ResourceNotFoundException {
        Optional<Brand> brand = brandRepository.findById(theId);
        if(!brand.isPresent()) {
            throw  new ResourceNotFoundException("Not found brand with ID=" + theId);
        } else {
            brand.get().setThumbnail(Base64Utils.encodeToString(brand.get().getThumbnailArr()));
            return brand.get();
        }
    }

    @Override
    public MessageResponse createBrand(BrandDTO theBrandDto) {

        Brand theBrand = new Brand();

        theBrand.setCreatedDate(new Date());
        theBrand.setCreatedBy(theBrandDto.getCreatedBy());
        theBrand.setName(theBrandDto.getName());
        theBrand.setDescription(theBrandDto.getDescription());
        if(theBrandDto.getThumbnail() != null) {
            theBrand.setThumbnailArr(Base64Utils.decodeFromString(theBrandDto.getThumbnail()));
        } else {
            theBrand.setThumbnailArr(new byte[0]);
        }

        brandRepository.save(theBrand);

        return new MessageResponse("Create brand successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateBrand(Long theId, BrandDTO theBrandDto) {
        Optional<Brand> theBrand = brandRepository.findById(theId);

        if(!theBrand.isPresent()) {
            throw new ResourceNotFoundException("Not found brand with ID=" + theId);
        } else {

            if(!theBrand.get().getName().equals(theBrandDto.getName())){
                if(brandRepository.existsByName(theBrandDto.getName())){
                    return new MessageResponse("Error: Brand name is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
                }
            }

            theBrand.get().setModifiedDate(new Date());
            theBrand.get().setModifiedBy(theBrandDto.getModifiedBy());
            theBrand.get().setName(theBrandDto.getName());
            theBrand.get().setDescription(theBrandDto.getDescription());
            if(theBrandDto.getThumbnail() != null) {
                theBrand.get().setThumbnailArr(Base64Utils.decodeFromString(theBrandDto.getThumbnail()));
            } else {
                theBrand.get().setThumbnailArr(new byte[0]);
            }

            brandRepository.save(theBrand.get());
        }

        return new MessageResponse("Updated brand successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteBrand(Long theId) {
        Brand theBrand = brandRepository.findById(theId).orElseThrow(
                () -> new ResourceNotFoundException("Not found brand with ID=" + theId));

        brandRepository.delete(theBrand);
    }

    @Override
    public Page<Brand> findAllPageAndSort(Pageable pagingSort) {
        Page<Brand> brandPage =  brandRepository.findAll(pagingSort);

        for(Brand brand : brandPage.getContent()) {
            brand.setThumbnail(Base64Utils.encodeToString(brand.getThumbnailArr()));
        }
        return  brandPage;
    }

    @Override
    public Page<Brand> findByNameContaining(String brandName, Pageable pagingSort) {
        Page<Brand> brandPage =  brandRepository.findByNameContainingIgnoreCase(brandName, pagingSort);

        for(Brand brand : brandPage.getContent()) {
            brand.setThumbnail(Base64Utils.encodeToString(brand.getThumbnailArr()));
        }
        return  brandPage;
    }

    @Override
    public Boolean existsByName(String name) {
        return brandRepository.existsByName(name);
    }

    @Override
    public Brand findByName(String brandName) {
        Optional<Brand> brand = brandRepository.findByName(brandName);
        if(!brand.isPresent()) {
            throw  new ResourceNotFoundException("Not found brand with name=" + brandName);
        } else {
            brand.get().setThumbnail(Base64Utils.encodeToString(brand.get().getThumbnailArr()));
            return brand.get();
        }
    }
}
