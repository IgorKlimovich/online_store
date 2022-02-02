package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.exception.ProductNotFoundException;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    /**
     * Returns a list all product
     * @return list of product
     */
    List<ProductDto> findAll();

    /**
     * Returns the products from database by id
     * @param id product id for search
     * @return product by id
     * @throws ProductNotFoundException if there is no products with the id in database
     */
    ProductDto findById(Integer id);

    /**
     * Returns a list products from database by name
     * @param name product name for search
     * @return list of product
     */
    List<ProductDto> findAllByName(String name);

    /**
     * Returns the products from database by id
     * @param id product id for search
     * @return product by id
     * @throws ProductNotFoundException if there is no products with the id in database
     */
    Product findProductById(Integer id);

    /**
     * Returns a list products from database by list ids
     * @param id product id for search
     * @return list of product
     */
    List<ProductDto> findAllByIds(List<Integer> id);

    /**
     * Save the product in the database
     * @param productDto product for save
     * @param categoryName name product category for the product
     * @param file photo for the product
     */
    void save(ProductDto productDto, String categoryName, MultipartFile file);

    /**
     * This method update the card in the database and return updated product
     * @param product product for update
     * @return updated product
     */
    ProductDto update(ProductDto product);

    /**
     * Removes the product from the database
     * @param productDto product for remove
     */
    void delete(ProductDto productDto);

    /**
     * This method returns the last 5 products from the database, if there are fewer products it returns all
     * @return list of product
     */
    List<ProductDto> findLast();

    /**
     * adds product photo
     * @param id id of the product to which the photo will be added
     * @param file photo for the product
     * @return updated product
     */
    ProductDto addPhoto(Integer id, MultipartFile file);

    /**
     * Removes product photo
     * @param id product id whose photo will be deleted
     * @return updated product
     */
    ProductDto deletePhoto(Integer id);

}
