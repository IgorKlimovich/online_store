package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.model.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> findAll();
    ProductCategory findByName(String name);
}
