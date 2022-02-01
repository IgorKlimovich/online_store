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
import org.academy.OnlineStoreDemo.util.UtilListMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UtilServiceImplTest {

    @Autowired
    private UtilService utilService;

    @MockBean
    private ProductCategoryRepository productCategoryRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private ProductCategory productCategory;

    private Product product;

    private List<ProductDto> productsDto;

    private UserDto userDto;

    private List<UserDto> usersDto;

    private List<ProductCategoryDto> productCategoriesDto;

    @Autowired
    private UtilListMapper utilListMapper;

    private ProductCategoryDto productCategoryDto;

    private ProductCategoryDto productCategoryDto1;

    @BeforeEach
    public void setUp() {
        productCategory = new ProductCategory();
        productCategory.setId(1);
        productCategory.setName("category name");
        product = new Product();
        Product product1 = new Product();
        product.setId(1);
        product1.setId(2);
        product.setName("name");
        product1.setName("name1");
        product.setPrice(50.0);
        product1.setPrice(100.0);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product1);
        productCategory.setProducts(products);
        userDto = new UserDto();
        UserDto userDto1 = new UserDto();
        userDto.setId(1);
        userDto1.setId(2);
        userDto.setLogin("name");
        userDto1.setLogin("login");
        userDto.setPhoneNumber("6489");
        userDto1.setPhoneNumber("5489");
        userDto.setFirstName("name");
        userDto1.setFirstName("first name");
        usersDto = new ArrayList<>();
        usersDto.add(userDto);
        usersDto.add(userDto1);
        ProductDto productDto = new ProductDto();
        ProductDto productDto1 = new ProductDto();
        productDto.setName("product name");
        productDto1.setName("name");
        productDto.setPrice(150.0);
        productDto1.setPrice(100.0);
        productsDto = new ArrayList<>();
        productsDto.add(productDto);
        productsDto.add(productDto1);
        productCategoryDto = new ProductCategoryDto();
        productCategoryDto1 = new ProductCategoryDto();
        productCategoryDto.setName("name");
        productCategoryDto1.setName("category");
        productCategoryDto.setAmount(5);
        productCategoryDto1.setAmount(2);
        productCategoryDto.setId(1);
        productCategoryDto1.setId(2);
        productCategoriesDto = new ArrayList<>();
        productCategoriesDto.add(productCategoryDto);
        productCategoriesDto.add(productCategoryDto1);
    }

    @Test
    void findBySearchParameters() {
        when(productCategoryRepository.findByName(productCategory.getName())).thenReturn(productCategory);
        when(productRepository.existsProductByName(product.getName())).thenReturn(true);
        utilService.findBySearchParameters(productCategory.getName(), product.getName(), "5.0", "200.0");
        assertEquals(1, utilService.findBySearchParameters(productCategory.getName(), product.getName(),
                "5.0", "200.0").size());
    }

    @Test
    void findBySearchParametersNotExistProduct() {
        when(productCategoryRepository.findByName(productCategory.getName())).thenReturn(productCategory);
        when(productRepository.existsProductByName(product.getName())).thenReturn(false);
        utilService.findBySearchParameters(productCategory.getName(), product.getName(), "5.0", "200.0");
        assertEquals(2, utilService.findBySearchParameters(productCategory.getName(), product.getName(),
                "5.0", "200.0").size());
    }

    @Test
    void findBySearchParametersMorePrice() {
        product.setPrice(300.0);
        when(productCategoryRepository.findByName(productCategory.getName())).thenReturn(productCategory);
        when(productRepository.existsProductByName(product.getName())).thenReturn(true);
        utilService.findBySearchParameters(productCategory.getName(), product.getName(), "5.0", "200.0");
        assertEquals(0, utilService.findBySearchParameters(productCategory.getName(), product.getName(),
                "5.0", "200.0").size());
    }

    @Test
    void sortUsersByLogin() {
        List<User> users = utilListMapper.mapList(usersDto,User.class);
        when(userRepository.findAll()).thenReturn(users);
        utilService.sortUsersByParameters("login");
        assertEquals("login", utilService.sortUsersByParameters("login").get(0).getLogin());
    }

    @Test
    void sortUsersByPhoneNumber() {
        List<User> users = utilListMapper.mapList(usersDto,User.class);
        when(userRepository.findAll()).thenReturn(users);
        utilService.sortUsersByParameters("phoneNumber");
        assertEquals("5489", utilService.sortUsersByParameters("phoneNumber")
                .get(0).getPhoneNumber());
    }

    @Test
    void sortUsersByFirstName() {
        List<User> users = utilListMapper.mapList(usersDto,User.class);
        when(userRepository.findAll()).thenReturn(users);
        utilService.sortUsersByParameters("firstName");
        assertEquals("first name", utilService.sortUsersByParameters("firstName")
                .get(0).getFirstName());
    }

    @Test
    void findUserByParametersByLogin() {
        User user = modelMapper.map(userDto, User.class);
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(user);
        utilService.findUserByParameters("login", userDto.getLogin());
        assertEquals(1, utilService.findUserByParameters("login", userDto.getLogin()).size());
    }

    @Test
    void findUserByParametersByPhoneNumber() {
        User user = modelMapper.map(userDto, User.class);
        when(userRepository.findByPhoneNumber(userDto.getPhoneNumber())).thenReturn(user);
        utilService.findUserByParameters("phoneNumber", userDto.getPhoneNumber());
        assertEquals(1, utilService.findUserByParameters("phoneNumber", userDto.getPhoneNumber()).size());
    }

    @Test
    void sortProductByName() {
        utilService.sortProductByParameters(productsDto, "name");
        assertEquals("name", utilService.sortProductByParameters(productsDto, "name").get(0).getName());
    }

    @Test
    void sortProductByPrice() {
        utilService.sortProductByParameters(productsDto, "price");
        assertEquals(100.0, utilService.sortProductByParameters(productsDto, "price").get(0).getPrice());
    }

    @Test
    void sortProductCategoriesByParametersByName() {
        List<ProductCategory> productCategories = utilListMapper.mapList(productCategoriesDto,ProductCategory.class);
        when(productCategoryRepository.findAll()).thenReturn(productCategories);
        utilService.sortProductCategoriesByParameters(Stream.of(1,2).collect(Collectors.toList()), "name");
        assertEquals("category", utilService
                .sortProductCategoriesByParameters(Stream.of(1,2).collect(Collectors.toList()), "name").get(0).getName());
    }

    @Test
    void sortProductCategoriesByParametersByAmount() {
        List<ProductCategory> productCategories = utilListMapper.mapList(productCategoriesDto,ProductCategory.class);
        when(productCategoryRepository.findAll()).thenReturn(productCategories);
        utilService.sortProductCategoriesByParameters(Stream.of(1,2).collect(Collectors.toList()), "amount");
        assertEquals(2, utilService
                .sortProductCategoriesByParameters(Stream.of(1,2).collect(Collectors.toList()), "amount").get(0).getAmount());
    }
}