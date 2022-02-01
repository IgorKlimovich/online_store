package org.academy.OnlineStoreDemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.User;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.model.repository.ProductRepository;
import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.*;
import org.academy.OnlineStoreDemo.util.UtilListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class UtilServiceImpl implements UtilService {

    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final UtilListMapper utilListMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductDto> findBySearchParameters(String productCategoryName,
                                                   String productName, String minPrice, String maxPrice) {
        List<Product> products = productCategoryRepository.findByName(productCategoryName).getProducts();
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
            List<Product> productList = products.stream().filter(item -> item.getPrice() > min && item.getPrice() < max)
                    .collect(Collectors.toList());
            log.info("find product by search parameters: founded {}", productList.size());
            return utilListMapper.mapList(productList,ProductDto.class);
        }
        List<Product> productList = filteredProductsByName.stream().filter(item ->
                item.getPrice() > min && item.getPrice() < max
        ).collect(Collectors.toList());
        log.info("in find product by search parameters:founded {}", productList.size());
        return utilListMapper.mapList(productList,ProductDto.class);
    }

    @Override
    public List<UserDto> sortUsersByParameters(String parameter) {
        List<UserDto> usersDto = utilListMapper.mapList(userRepository.findAll(), UserDto.class);
        if (parameter.equals("login")) {
            log.info("in sort users by parameters: sorted {} users by login", usersDto.size());
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getLogin)).collect(Collectors.toList());
        }
        if (parameter.equals("email")) {
            log.info("in sort users by parameters: sorted {} users by email ", usersDto.size());
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getEmail)).collect(Collectors.toList());
        }
        if (parameter.equals("phoneNumber")) {
            log.info("in sort users by parameters: sorted {} users by phone number ", usersDto.size());
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getPhoneNumber)).collect(Collectors.toList());
        }
        if (parameter.equals("firstName")) {
            log.info("in sort users by parameters: sorted {} users by firstName ", usersDto.size());
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getFirstName)).collect(Collectors.toList());
        }
        if (parameter.equals("lastName")) {
            log.info("in sort users by parameters: sorted {} users by lastName ", usersDto.size());
            return usersDto.stream().sorted(Comparator.comparing(UserDto::getLastName)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<UserDto> findUserByParameters(String parameter, String name) {
        if (parameter.equals("all")) {
            return utilListMapper.mapList(userRepository.findAll(), UserDto.class);
        }
        if (parameter.equals("login")) {
            User user = userRepository.findByLogin(name.trim());
            if (user == null) {
                log.warn("in find user by parameters: user not found by parameter {} name {}", parameter, name);
                return Collections.emptyList();
            }
            log.info("in find user by parameters: user {} found by parameter {} name {}", user, parameter, name);
            return Collections.singletonList(modelMapper.map(user, UserDto.class));
        }
        if (parameter.equals("phoneNumber")) {
            User user = userRepository.findByPhoneNumber(name.trim());
            if (user == null) {
                log.warn("in find user by parameters: user not found by parameter {} name {}", parameter, name);
                return Collections.emptyList();
            }
            log.info("in find user by parameters: user {} found by parameter {} name{}", user, parameter, name);
            return Collections.singletonList(modelMapper.map(user, UserDto.class));
        }
        if (parameter.equals("email")) {
            User user = userRepository.findByEmail(name.trim());
            if (user == null) {
                log.warn("in find user by parameters: user not found by parameter {} name{}", parameter, name);
                return Collections.emptyList();
            }
            log.info("in find user by parameters: user {} found by parameter {} name{}", user, parameter, name);
            return Collections.singletonList(modelMapper.map(user, UserDto.class));
        }
        return Collections.singletonList(new UserDto());
    }


    @Override
    public List<ProductDto> sortProductByParameters(List<ProductDto> productsDto, String parameter) {
        if (parameter.equals("name")) {
            log.info("in sort users by parameters: sorted {} products by name", productsDto.size());
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getName)).collect(Collectors.toList());
        }
        if (parameter.equals("category")) {
            log.info("in sort users by parameters: sorted {} products by category", productsDto.size());
            return productsDto.stream().sorted(Comparator.comparing(prod -> prod.getProductCategoryDto().getName())).collect(Collectors.toList());
        }
        if (parameter.equals("price")) {
            log.info("in sort users by parameters: sorted {} products by price", productsDto.size());
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getPrice)).collect(Collectors.toList());
        }
        if (parameter.equals("description")) {
            log.info("in sort users by parameters: sorted {} products by description", productsDto.size());
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getDescription)).collect(Collectors.toList());
        }
        if (parameter.equals("isExist")) {
            log.info("in sort users by parameters: sorted {} products by is exist", productsDto.size());
            return productsDto.stream().sorted(Comparator.comparing(ProductDto::getIsExist)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ProductCategoryDto> sortProductCategoriesByParameters(List<Integer> ids, String parameter) {
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAllByIds(ids);
        if (parameter.equals("name")) {
            log.info("in sort product categories by parameters: sorted productCategories by parameter {}", parameter);
            return productCategoriesDto.stream()
                    .sorted(Comparator.comparing(ProductCategoryDto::getName))
                    .collect(Collectors.toList());
        }
        if (parameter.equals("amount")) {
            log.info("in sort product categories by parameters: sorted  productCategories by parameter {}", parameter);
            return productCategoriesDto.stream()
                    .sorted(Comparator.comparing(ProductCategoryDto::getAmount))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void savePhotoWithPath(String uploadDir, String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir);
        try {
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            InputStream inputStream = multipartFile.getInputStream();
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("in save photo with path: error save file",e);
        }
        log.info("in save photo with path name: file and path saved");
    }

    @Override
    public List<OrderDto> findOrdersByParameters(String parameter) {
        List<OrderDto> ordersDto = orderService.findAll();
        if (parameter.equals("all")) {
            log.info("in find orders by parameters: founded {} orders", ordersDto.size());
            return ordersDto;
        }
        return ordersDto.stream().filter(item -> item.getStateOrderDto().getName().equals(parameter.toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findProductsByParameters(String parameter, String name) {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productsDto = utilListMapper.mapList(products,ProductDto.class);
        if (parameter.equals("allProd")) {
            log.info("in find product by parameters: founded {} products", products.size());
            return productsDto;
        }
        if (parameter.equals("category")) {
            ProductCategoryDto productCategoryDto = productCategoryService.findByName(name.trim());
            log.info("in find product by parameters: founded {} products", productCategoryDto.getProductsDto().size());
            return productCategoryDto.getProductsDto();
        }
        if (parameter.equals("nameProd")) {
            return productsDto.stream().filter(item -> item.getName().equals(name.trim())).collect(Collectors.toList());
        }
        return productsDto;
    }

    @Override
    public List<ProductCategoryDto> findProductCategoriesByParameters(String parameter, String name) {
        if (parameter.equals("allCategories")) {
            return productCategoryService.findAll();
        }
        if (parameter.equals("categoryName")) {
            ProductCategoryDto productCategoryDto = productCategoryService.findCategoryByName(name.trim());
            if (productCategoryDto.getId()==null) {
                log.info("in find category by parameters: category not found");
                return Collections.emptyList();
            }
            log.info("in find category by parameters: founded category {}",productCategoryDto);
            return Collections.singletonList(productCategoryDto);
        }
        return Collections.singletonList(new ProductCategoryDto());
    }

    @Override
    public List<ProductDto> headerSearch(String name) {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productsDto = utilListMapper.mapList(products, ProductDto.class);
        return  productsDto.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.trim().toLowerCase()))
                .collect(Collectors.toList());
    }
}