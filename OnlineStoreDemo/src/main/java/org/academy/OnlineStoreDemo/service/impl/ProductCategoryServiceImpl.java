package org.academy.OnlineStoreDemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.exception.ProductCategoryNotFoundException;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.util.UtilListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ModelMapper modelMapper;
    private final UtilListMapper utilListMapper;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategoryDto> findAll() {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        log.info("in find all product categories: founded {} product categories", productCategories.size());
        return utilListMapper.mapList(productCategories,ProductCategoryDto.class);
    }

    @Override
    public ProductCategoryDto findByName(String name) {
        ProductCategory productCategory = productCategoryRepository.findByName(name);
        log.info("in find product category by name: product category {} founded by name {}", productCategory, name);
        return modelMapper.map(productCategory, ProductCategoryDto.class);
    }

    @Override
    public ProductCategoryDto findCategoryByName(String name){
        ProductCategory productCategory =
                productCategoryRepository.findProductCategoryByName(name).orElse(new ProductCategory());
        log.info("in find product category by name: product category {} founded by name {}", productCategory, name);
        return modelMapper.map(productCategory, ProductCategoryDto.class);
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
        log.info("in find product categories by ids: founded {} product categories ", res.size());
        return utilListMapper.mapList(productCategories,ProductCategoryDto.class);
    }

    @Override
    public ProductCategoryDto findById(Integer id) {
        ProductCategory productCategory = productCategoryRepository
                        .findById(id).orElseThrow(()->new ProductCategoryNotFoundException("product category not found"));
        log.info("in find by id product category : product category {} founded by id {}", productCategory, id);
        return modelMapper.map(productCategory, ProductCategoryDto.class);
    }

    @Override
    public ProductCategoryDto update(ProductCategoryDto productCategoryDto) {
        ProductCategoryDto forUpdateProductCategoryDto = findById(productCategoryDto.getId());
        forUpdateProductCategoryDto.setName(productCategoryDto.getName());
        ProductCategory productCategoryForUpdate = modelMapper.map(forUpdateProductCategoryDto, ProductCategory.class);
        ProductCategory productCategory = productCategoryRepository.save(productCategoryForUpdate);
        log.info("in update product category: product category {} updated", productCategory);
        return modelMapper.map(productCategory, ProductCategoryDto.class);
    }

    @Override
    public void delete(ProductCategoryDto productCategoryDto) {
        ProductCategory productCategory = modelMapper.map(productCategoryDto, ProductCategory.class);
        productCategoryRepository.delete(productCategory);
        log.info("in delete product category: product category {} deleted", productCategory);
    }
}
