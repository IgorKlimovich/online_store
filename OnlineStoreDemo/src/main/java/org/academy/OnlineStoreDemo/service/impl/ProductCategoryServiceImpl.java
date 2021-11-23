package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.model.ProductCategory;
import org.academy.OnlineStoreDemo.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }


    public List<ProductCategory> findAll(){
        return  productCategoryRepository.findAll();
    }

    public ProductCategory findByName(String name){
       return productCategoryRepository.findByName(name);
    }
}
