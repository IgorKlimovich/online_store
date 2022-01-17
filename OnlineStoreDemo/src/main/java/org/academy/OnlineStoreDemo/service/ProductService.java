package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();

    ProductDto findById(Integer id);

    Boolean existsProductByName(String name);

    List<ProductDto> findAllByName(String name);

    List<ProductDto> findAllByIds(List<Integer> id);

    void saveWithCategoryName(ProductDto productDto, String categoryName);

    void update(ProductDto product);

    void delete(ProductDto productDto);

    List<ProductDto> findLast();

    ProductDto findByPhotoName(String photoName);

    void addPhoto(ProductDto productDto);

    void deletePhoto(ProductDto productDto);

}
