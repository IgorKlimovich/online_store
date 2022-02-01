package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UtilService {

    /**
     * Returns a list of products according to the given parameters
     * @param productCategoryName product category name for search
     * @param productName product name for search
     * @param minPrice min price for search
     * @param maxPrice max price for search
     * @return list of products
     */
    List<ProductDto> findBySearchParameters(String productCategoryName, String productName, String minPrice,
                                            String maxPrice);

    /**
     * Returns a list of users sorted by the given parameter
     * @param parameter the parameter by which the list will be sorted
     * @return list of users
     */
    List<UserDto> sortUsersByParameters(String parameter);

    /**
     * Returns a list of users according to the given parameters
     * @param parameter users search parameter
     * @param name search name
     * @return list of users
     */
    List<UserDto> findUserByParameters(String parameter, String name);

    /**
     * Returns a list of products sorted by the given parameter
     * @param productsDto list of products to sort
     * @param parameter the parameter by which the list will be sorted
     * @return list of products
     */
    List<ProductDto> sortProductByParameters(List<ProductDto> productsDto, String parameter);

    /**
     * Returns a list of product categories sorted by the given parameter
     * @param ids list of product categories ids to sort
     * @param parameter the parameter by which the list will be sorted
     * @return list of product categories
     */
    List<ProductCategoryDto> sortProductCategoriesByParameters(List<Integer> ids, String parameter);

    /**
     * This method saves the photo to the hard drive
     * @param uploadDir folder where the file will be saved
     * @param fileName file to save
     * @param multipartFile file
     */
    void savePhotoWithPath(String uploadDir, String fileName, MultipartFile multipartFile);

    /**
     * Returns a list of orders according to the given parameters
     * @param parameter orders search parameter
     * @return list of orders
     */
    List<OrderDto> findOrdersByParameters(String parameter);

    /**
     * Returns a list of products according to the given parameters
     * @param parameter products search parameter
     * @param name search name
     * @return list of products
     */
    List<ProductDto> findProductsByParameters(String parameter, String name);

    /**
     * Returns a list of product categories according to the given parameters
     * @param parameter product categories search parameter
     * @param name search name
     * @return list of product categories
     */
    List<ProductCategoryDto> findProductCategoriesByParameters(String parameter, String name);

    /**
     * Returns a list of product by product name
     * @param name product name
     * @return list of products
     */
    List<ProductDto> headerSearch(String name);
}
