package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UtilService {

    List<ProductDto> findBySearchParameters(String productCategoryName,
                                         String productName, String minPrice, String maxPrice);

    List<UserDto> sortUsersByParameters(List<UserDto> users, String parameter);

    UserDto findUserByParameters(String parameter, String name);

    List<ProductDto> sortProductByParameters(List<ProductDto> productsDto, String parameter);

    List<ProductCategoryDto>
    sortProductCategoriesByParameters(List<ProductCategoryDto> productCategoriesDto, String parameter);

    void savePhotoWithPath(String uploadDir, String fileName, MultipartFile multipartFile);

}
