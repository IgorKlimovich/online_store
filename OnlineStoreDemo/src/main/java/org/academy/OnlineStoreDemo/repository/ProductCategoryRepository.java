package org.academy.OnlineStoreDemo.repository;

import org.academy.OnlineStoreDemo.model.ProductCategory;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    ProductCategory findByName(String productCategoryName);
}


