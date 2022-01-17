package org.academy.OnlineStoreDemo.service.impl;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
            List<ProductDto> productsDto =new ArrayList<>();
            for (Product product : productList) {
                ProductDto map = modelMapper.map(product, ProductDto.class);
                productsDto.add(map);
            }
            log.info("find product by search parameters: founded {}",productList.size());
            return productsDto;
        }

        List<Product> productList=filteredProductsByName.stream().filter(item ->
                item.getPrice() > min && item.getPrice() < max
        ).collect(Collectors.toList());
        List<ProductDto> productsDto =new ArrayList<>();
        for (Product product : productList) {
            ProductDto map = modelMapper.map(product, ProductDto.class);
            productsDto.add(map);
        }
        log.info("in find product by search parameters:founded {}",productList.size());
        return productsDto;
    }

    @Override
    public List<UserDto> sortUsersByParameters(List<UserDto> usersDto, String parameter) {

        if (parameter.equals("login")) {
            log.info("in sort users by parameters: sorted {} users by parameter {}",usersDto.size(),parameter);
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getLogin)).collect(Collectors.toList());
        }
        if (parameter.equals("email")) {
            log.info("in sort users by parameters: sorted {} users by parameter {}",usersDto.size(),parameter);
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getEmail)).collect(Collectors.toList());
        }
        if (parameter.equals("phoneNumber")) {
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getPhoneNumber)).collect(Collectors.toList());
        }
        if (parameter.equals("firstName")) {
            log.info("in sort users by parameters: sorted {} users by parameter {}",usersDto.size(),parameter);
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getFirstName)).collect(Collectors.toList());
        }
        if (parameter.equals("lastName")) {
            log.info("in sort users by parameters: sorted {} users by parameter {}",usersDto.size(),parameter);
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getLastName)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public UserDto findUserByParameters(String parameter, String name) {
        if (parameter.equals("login")) {
            User user= userRepository.findByLogin(name.trim());
            if (user==null){
                log.warn("in find user by parameters: user not found by parameter {} name {}",parameter,name );
                return null;
            }
            log.info("in find user by parameters: user {} found by parameter {} name {}",user,parameter,name );
           return modelMapper.map(user,UserDto.class);
        }
        if (parameter.equals("phoneNumber")) {
           User user= userRepository.findByPhoneNumber(name.trim());
            if (user==null){
                log.warn("in find user by parameters: user not found by parameter {} name {}",parameter,name );
                return null;
            }
            log.info("in find user by parameters: user {} found by parameter {} name{}",user,parameter,name );
           return modelMapper.map(user,UserDto.class);
        }
        if (parameter.equals("email")) {
            User user = userRepository.findByEmail(name.trim());
            if (user==null){
                log.warn("in find user by parameters: user not found by parameter {} name{}",parameter,name );
                return null;
            }
            log.info("in find user by parameters: user {} found by parameter {} name{}",user,parameter,name );
            return modelMapper.map(user, UserDto.class);
        }
        return new UserDto();
    }

    @Override
    public List<ProductDto> sortProductByParameters(List<ProductDto> productsDto, String parameter) {
        if (parameter.equals("name")) {
            log.info("in sort users by parameters: sorted {} products by parameter {}",productsDto.size(),parameter);
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getName)).collect(Collectors.toList());
        }
        if (parameter.equals("category")) {
            log.info("in sort users by parameters: sorted {} products by parameter {}",productsDto.size(),parameter);
            return productsDto.stream().sorted(Comparator.comparing(prod -> prod.getProductCategoryDto().getName())).collect(Collectors.toList());
        }
        if (parameter.equals("price")) {
            log.info("in sort users by parameters: sorted {} products by parameter {}",productsDto.size(),parameter);
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getPrice)).collect(Collectors.toList());
        }
        if (parameter.equals("description")) {
            log.info("in sort users by parameters: sorted {} products by parameter {}",productsDto.size(),parameter);
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getDescription)).collect(Collectors.toList());
        }
        if (parameter.equals("isExist")) {
            log.info("in sort users by parameters: sorted {} products by parameter {}",productsDto.size(),parameter);
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getIsExist)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ProductCategoryDto> sortProductCategoriesByParameters(
            List<ProductCategoryDto> productCategoriesDto, String parameter) {
        if (parameter.equals("name")) {
            List<ProductCategoryDto> sortedProductCategoriesDto=productCategoriesDto
                    .stream()
                    .sorted(Comparator.comparing(ProductCategoryDto::getName))
                    .collect(Collectors.toList());
            log.info("in sort product categories by parameters: sorted {} productCategories by parameter {}",
                    sortedProductCategoriesDto.size(),parameter);
            return sortedProductCategoriesDto;
        }
        if (parameter.equals("amount")) {
            List<ProductCategoryDto> sortedProductCategoriesDto=productCategoriesDto
                    .stream()
                    .sorted(Comparator.comparing(ProductCategoryDto::getAmount))
                    .collect(Collectors.toList());
            log.info("in sort product categories by parameters: sorted {} productCategories by parameter {}",
                    sortedProductCategoriesDto.size(),parameter);
            return sortedProductCategoriesDto;
        }
        return new ArrayList<>();

    }

    @Override
    public void savePhotoWithPath(String uploadDir, String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir);
//        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                InputStream inputStream = multipartFile.getInputStream();
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.error("in save photo with path: directories not created");
            }
//        }
        log.info("in save photo with path name: file and path saved");
    }


}
