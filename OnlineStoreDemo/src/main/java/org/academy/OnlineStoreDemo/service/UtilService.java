package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.entity.User;

import java.util.List;

public interface UtilService {

    List<ProductDto> findBySearchParameters(String productCategoryName,
                                         String productName, String minPrice, String maxPrice);

    List<User> sortUsersByParameters(List<User> users, String parameter);

    User findUserByParameters(String parameter, String name);

    List<ProductDto> sortProductByParameters(List<ProductDto> productDtos, String parameter);

    List<ProductCategoryDto>
    sortProductCategoriesByParameters(List<ProductCategoryDto> productCategoryDtos, String parameter);
}
