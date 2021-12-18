package org.academy.OnlineStoreDemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ModelMapper modelMapper;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository, ModelMapper modelMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductCategoryDto> findAll() {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        log.info("in find all product categories: founded {} product categories", productCategories.size());
        List<ProductCategoryDto> productCategoriesDto= new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            ProductCategoryDto map = modelMapper.map(productCategory, ProductCategoryDto.class);
            productCategoriesDto.add(map);
        }
        return productCategoriesDto;
    }

    @Override
    public ProductCategoryDto findByName(String name) {
        ProductCategory productCategory = productCategoryRepository.findByName(name);
        if (productCategory==null){
            log.warn("in find product category by name: product category not found by name {}",name);
            return null;
        }
        log.info("in find product category by name: product category {} founded by name {}", productCategory, name);
        return modelMapper.map(productCategory,ProductCategoryDto.class);
    }

    @Override
    public Boolean existsProductCategoryByName(String name) {
        return productCategoryRepository.existsProductCategoryByName(name);
    }

    @Override
    public void save(String name) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(name);
        productCategory.setAmount(0);
        productCategoryRepository.save(productCategory);
        log.info("in save product category: product category {} saved", productCategory);
    }

    @Override
    public List<ProductCategoryDto> findAllByIds(List<Integer> id) {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        List<ProductCategory> res = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            if (id.contains(productCategory.getId())) {
                res.add(productCategory);
            }
        }
        List<ProductCategoryDto> productCategoriesDto= new ArrayList<>();
        for (ProductCategory productCategory:res){
            ProductCategoryDto map = modelMapper.map(productCategory, ProductCategoryDto.class);
            productCategoriesDto.add(map);
        }
        log.info("in find product categories by ids: founded {} product categories ", res.size());
        return productCategoriesDto;
    }

    @Override
    public ProductCategoryDto findById(Integer id) {
        ProductCategory productCategory = null;
        try {
            productCategory = productCategoryRepository.findById(id).orElseThrow(Exception::new);
        } catch (Exception e) {
            log.warn("in find by id product category : product category not found by id {}", id);
        }
        log.info("in find by id product category : product category {} founded by id {}", productCategory, id);
        return modelMapper.map(productCategory, ProductCategoryDto.class);

    }

    public void update(ProductCategoryDto productCategoryDto) {
        ProductCategoryDto forUpdateProductCategoryDto = findById(productCategoryDto.getId());
        forUpdateProductCategoryDto.setName(productCategoryDto.getName());
        ProductCategory productCategoryforUpdate=modelMapper.map(forUpdateProductCategoryDto,ProductCategory.class);
        productCategoryRepository.save(productCategoryforUpdate);
        log.info("in update product category: product category {} updated", productCategoryforUpdate);
    }

    @Override
    public void delete(ProductCategoryDto productCategoryDto) {
        ProductCategory productCategory =modelMapper.map(productCategoryDto,ProductCategory.class);
        productCategoryRepository.delete(productCategory);
        log.info("in delete product category: product category {} deleted", productCategory);
    }
}
