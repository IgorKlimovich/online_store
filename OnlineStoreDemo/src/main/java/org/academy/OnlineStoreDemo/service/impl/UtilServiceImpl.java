package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.model.Product;
import org.academy.OnlineStoreDemo.model.ProductCategory;
import org.academy.OnlineStoreDemo.model.User;
import org.academy.OnlineStoreDemo.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.repository.ProductRepository;
import org.academy.OnlineStoreDemo.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilServiceImpl implements UtilService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;


    public UtilServiceImpl(UserRepository userRepository, ProductRepository productRepository
            , ProductCategoryRepository productCategoryRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public boolean isExistUserByLogin(String login) {
        return userRepository.findByLogin(login) != null;
    }

    public boolean isExistUserByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean isExistUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber) != null;
    }

    public boolean isExistProductByName(String name) {
        return productRepository.findByName(name) != null;
    }

    public boolean isExistProductCategoryByName(String name) {
        return productCategoryRepository.findByName(name) != null;
    }

    public List<Product> findBySearchParameters(String productCategoryName,
                                                String productName, String minPrice, String maxPrice) {

        ProductCategory productCategory = productCategoryRepository.findByName(productCategoryName);
        Double min = Double.parseDouble(minPrice);
        Double max = Double.parseDouble(maxPrice);
        List<Product> products = productCategory.getProducts();
        List<Product> filteredProductsByName = products.stream()
                .filter(product -> product.getName().equals(productName)).collect(Collectors.toList());
        return filteredProductsByName.stream().filter(item ->
                item.getPrice() > min && item.getPrice() < max
        ).collect(Collectors.toList());
    }

    public List<User> sortUsersByParameters(List<User> users, String parameter) {

        if (parameter.equals("login")) {
            return users.stream().sorted(Comparator.comparing(User::getLogin)).collect(Collectors.toList());
        }
        if (parameter.equals("email")) {
            return users.stream().sorted(Comparator.comparing(User::getEmail)).collect(Collectors.toList());
        }
        if (parameter.equals("phoneNumber")) {
            return users.stream().sorted(Comparator.comparing(User::getPhoneNumber)).collect(Collectors.toList());
        }
        if (parameter.equals("firstName")) {
            return users.stream().sorted(Comparator.comparing(User::getFirstName)).collect(Collectors.toList());
        }
        if (parameter.equals("lastName")) {
            return users.stream().sorted(Comparator.comparing(User::getLastName)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public User findUserByParameters(String parameter, String name){
        if (parameter.equals("login")){
          return   userRepository.findByLogin(name);
        }
        if (parameter.equals("phoneNumber")){
            return userRepository.findByPhoneNumber(name);
        }
        if (parameter.equals("email")){
            return userRepository.findByEmail(name);
        }
        return null;
    }
}
