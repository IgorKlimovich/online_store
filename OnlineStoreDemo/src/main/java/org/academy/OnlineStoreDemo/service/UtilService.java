package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.model.Product;
import org.academy.OnlineStoreDemo.model.User;

import java.util.List;

public interface UtilService {
    boolean isExistUserByLogin(String login);
    boolean isExistUserByEmail(String email);
    boolean isExistUserByPhoneNumber(String phoneNumber);
    boolean isExistProductByName(String name);
    boolean isExistProductCategoryByName(String name);
    List<Product> findBySearchParameters(String productCategoryName,
                                        String productName, String minPrice, String maxPrice);
    List<User> sortUsersByParameters(List<User> users, String parameter);
    User findUserByParameters(String parameter, String name);
}
