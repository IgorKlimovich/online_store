package org.academy.OnlineStoreDemo.model.repository;

import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    ProductCategory findByName(String productCategoryName);
    Boolean existsProductCategoryByName(String name);
    Optional<ProductCategory> findById(Integer id);
    Optional<ProductCategory> findProductCategoryByName(String name);

}


