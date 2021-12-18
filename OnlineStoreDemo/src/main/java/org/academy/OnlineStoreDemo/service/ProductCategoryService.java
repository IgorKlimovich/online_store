package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategoryDto> findAll();

    ProductCategoryDto findByName(String name);

    Boolean existsProductCategoryByName(String name);

    void save(String name);

    List<ProductCategoryDto> findAllByIds(List<Integer> id);

    ProductCategoryDto findById(Integer id);

    void update(ProductCategoryDto productCategoryDto);

    void delete(ProductCategoryDto productCategoryDto);
}
