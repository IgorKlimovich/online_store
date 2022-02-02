package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.exception.ProductCategoryNotFoundException;

import java.util.List;

public interface ProductCategoryService {

    /**
     * Returns a list all product categories
     *
     * @return list of product categories
     */
    List<ProductCategoryDto> findAll();

    /**
     * Returns the product category by name
     *
     * @param name product name for search
     * @return product category by name
     */
    ProductCategoryDto findByName(String name);

    /**
     * Checks if the product category exists
     *
     * @param name product name for search
     * @return true if product exist
     */
    Boolean existsProductCategoryByName(String name);

    /**
     * Save the card
     *
     * @param name product name for save
     */
    void save(String name);

    /**
     * This method return list of product categories by list ids
     *
     * @param id list of ids product categories to search for
     * @return list of product categories
     */
    List<ProductCategoryDto> findAllByIds(List<Integer> id);

    /**
     * Returns the product categories from database by id
     *
     * @param id product category id for search
     * @return product category by id
     * @throws ProductCategoryNotFoundException if there is no product categories with the id in database
     */
    ProductCategoryDto findById(Integer id);

    /**
     * Update the product category in database
     *
     * @param productCategoryDto product category for update
     * @return an updated product category
     */
    ProductCategoryDto update(ProductCategoryDto productCategoryDto);

    /**
     * Removes the product category from the database
     *
     * @param productCategoryDto product category for remove
     */
    void delete(ProductCategoryDto productCategoryDto);

    /**
     * Returns the product category by name
     *
     * @param name product name for search
     * @return product category by name
     * @throws ProductCategoryNotFoundException if there is no product categories with the name in database
     */
    ProductCategoryDto findCategoryByName(String name);
}
