package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.model.repository.ProductRepository;
import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilServiceImpl implements UtilService {

    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public UtilServiceImpl(UserRepository userRepository,
                           ProductCategoryRepository productCategoryRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductDto> findBySearchParameters(String productCategoryName,
                                                String productName, String minPrice, String maxPrice) {

        ProductCategory productCategory = productCategoryRepository.findByName(productCategoryName);

        List<Product> products = productCategory.getProducts();

        List<Product> filteredProductsByName = products.stream()
                .filter(product -> product.getName().equals(productName)).collect(Collectors.toList());
        if (minPrice.equals("")) {
            minPrice = "0";
        }
        if (maxPrice.equals("")) {
            maxPrice = "1000000";
        }
        Double min = Double.parseDouble(minPrice);
        Double max = Double.parseDouble(maxPrice);
        if (Boolean.TRUE.equals(!productRepository.existsProductByName(productName)) || productName == null) {
            List<Product> productList= products.stream().filter(item -> item.getPrice() > min && item.getPrice() < max)
                    .collect(Collectors.toList());

            List<ProductDto> productDtos =new ArrayList<>();
            for (Product product : productList) {
                ProductDto map = modelMapper.map(product, ProductDto.class);
                productDtos.add(map);
            }
            return productDtos;

        }

        List<Product> productList=filteredProductsByName.stream().filter(item ->
                item.getPrice() > min && item.getPrice() < max
        ).collect(Collectors.toList());
        List<ProductDto> productDtos =new ArrayList<>();
        for (Product product : productList) {
            ProductDto map = modelMapper.map(product, ProductDto.class);
            productDtos.add(map);
        }
        return productDtos;
    }

    @Override
    public List<UserDto> sortUsersByParameters(List<UserDto> users, String parameter) {

        if (parameter.equals("login")) {
            return users.stream().sorted(Comparator.comparing(UserDto::getLogin)).collect(Collectors.toList());
        }
        if (parameter.equals("email")) {
            return users.stream().sorted(Comparator.comparing(UserDto::getEmail)).collect(Collectors.toList());
        }
        if (parameter.equals("phoneNumber")) {
            return users.stream().sorted(Comparator.comparing(UserDto::getPhoneNumber)).collect(Collectors.toList());
        }
        if (parameter.equals("firstName")) {
            return users.stream().sorted(Comparator.comparing(UserDto::getFirstName)).collect(Collectors.toList());
        }
        if (parameter.equals("lastName")) {
            return users.stream().sorted(Comparator.comparing(UserDto::getLastName)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public UserDto findUserByParameters(String parameter, String name) {
        if (parameter.equals("login")) {
            User user= userRepository.findByLogin(name);
           return modelMapper.map(user,UserDto.class);
        }
        if (parameter.equals("phoneNumber")) {
           User user= userRepository.findByPhoneNumber(name);
           return modelMapper.map(user,UserDto.class);
        }
        if (parameter.equals("email")) {
            User user = userRepository.findByEmail(name);
            return modelMapper.map(user, UserDto.class);
        }
        return null;
    }

    @Override
    public List<ProductDto> sortProductByParameters(List<ProductDto> productsDto, String parameter) {
        if (parameter.equals("name")) {
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getName)).collect(Collectors.toList());
        }
        if (parameter.equals("category")) {
            return productsDto.stream().sorted(Comparator.comparing(prod -> prod.getProductCategoryDto().getName())).collect(Collectors.toList());
        }
        if (parameter.equals("price")) {
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getPrice)).collect(Collectors.toList());
        }
        if (parameter.equals("description")) {
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getDescription)).collect(Collectors.toList());
        }
        if (parameter.equals("isExist")) {
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getIsExist)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ProductCategoryDto> sortProductCategoriesByParameters(
            List<ProductCategoryDto> productCategoriesDto, String parameter) {
        if (parameter.equals("name")) {
            return productCategoriesDto
                    .stream()
                    .sorted(Comparator.comparing(ProductCategoryDto::getName))
                    .collect(Collectors.toList());
        }
        if (parameter.equals("amount")) {
            return productCategoriesDto
                    .stream()
                    .sorted(Comparator.comparing(ProductCategoryDto::getAmount))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();

    }


}
