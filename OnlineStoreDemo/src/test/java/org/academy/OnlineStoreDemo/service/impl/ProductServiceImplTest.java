package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.model.repository.ProductRepository;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductCategoryRepository productCategoryRepository;

    private ProductDto productDto;

    private ProductCategory productCategory;

    private Product product;

    private List<Product> products;

    private List<ProductDto> productsDto;

    @BeforeEach
    public void setUp() {
        product = new Product();
        Product product1 = new Product();
        products = new ArrayList<>();
        product.setId(1);
        product.setName("name");
        product.setNamePhoto("photoName");
        product1.setId(2);
        product1.setName("name");
        products.add(product);
        products.add(product1);
        productDto = new ProductDto();
        productDto.setId(1);
        productDto.setName("product name");
        productDto.setPrice(50.0);
        productDto.setAmount(4);
        productDto.setIsExist(true);
        productDto.setDescription("description");
        productCategory = new ProductCategory();
        productCategory.setId(1);
        productCategory.setName("category name");
        productCategory.setAmount(5);
        ProductDto productDto1 = new ProductDto();
        productDto1.setId(2);
        productDto1.setName("product name2");
        productDto1.setPrice(55.0);
        productDto1.setAmount(4);
        productDto1.setIsExist(true);
        productDto1.setDescription("description2");
        productsDto = new ArrayList<>();
        productsDto.add(productDto);
        productsDto.add(productDto1);
//        product.setProductCategory(productCategory);
    }

    @Test
    void findAll() {
        when(productRepository.findAll()).thenReturn(products);
        List<ProductDto> productsDto = productService.findAll();
        verify(productRepository, times(1)).findAll();
        assertEquals(2, productsDto.size());
        assertEquals(1, productsDto.get(0).getId());
    }

    @Test
    void findById() {
        when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
        ProductDto productDto = productService.findById(1);
        verify(productRepository, times(1)).findById(1);
        assertEquals(1, productDto.getId());
    }

    @Test
    void findByIdFail() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        ProductDto productDto = productService.findById(1);
        verify(productRepository, times(1)).findById(1);
        assertNull(productDto);
    }


    @Test
    void existsProductByName() {
        when(productRepository.existsProductByName("name")).thenReturn(true);
        Boolean exist = productService.existsProductByName("name");
        verify(productRepository, times(1)).existsProductByName("name");
        assertTrue(exist);
    }

    @Test
    void existsProductByNameFail() {
        when(productRepository.existsProductByName("name")).thenReturn(false);
        Boolean notExist = productService.existsProductByName("name");
        verify(productRepository, times(1)).existsProductByName("name");
        assertFalse(notExist);
    }


    @Test
    void findAllByName() {
        when(productRepository.findAllByName("name")).thenReturn(products);
        List<ProductDto> productsDto = productService.findAllByName("name");
        assertEquals(2, productsDto.size());
        assertEquals(1, productsDto.get(0).getId());

    }

    @Test
    void findAllByNameFail() {
        when(productRepository.findAllByName("name")).thenReturn(Collections.emptyList());
        List<ProductDto> productsDto = productService.findAllByName("name");
        verify(productRepository, times(1)).findAllByName("name");
        assertEquals(0, productsDto.size());
    }

    @Test
    void findAllByIds() {
        List<Integer> ids = new ArrayList<>();
        ids.add(product.getId());
        when(productRepository.findAll()).thenReturn(products);
        List<ProductDto> productsDto = productService.findAllByIds(ids);
        verify(productRepository, times(1)).findAll();
        assertEquals(1, productsDto.size());
    }

    @Test
    void saveWithCategoryName() {
        Product product = modelMapper.map(productDto, Product.class);
        when(productCategoryRepository.findByName(productCategory.getName())).thenReturn(productCategory);
        productService.saveWithCategoryName(productDto, productCategory.getName());
        verify(productRepository, times(1)).save(product);
        verify(productCategoryRepository, times(1)).save(productCategory);
    }

    @Test
    void update() {
        ProductCategoryDto productCategoryDto = modelMapper.map(productCategory, ProductCategoryDto.class);
        productDto.setProductCategoryDto(productCategoryDto);
        Product product = modelMapper.map(productDto, Product.class);
        when(productRepository.findById(productDto.getId())).thenReturn(Optional.of(product));
        when(productCategoryRepository.findByName(productCategory.getName())).thenReturn(productCategory);
        productService.update(productDto);
        verify(productRepository, times(1)).save(product);
        verify(productCategoryRepository, times(2)).save(productCategory);
    }

    @Test
    void delete() {
        ProductCategoryDto productCategoryDto = modelMapper.map(productCategory, ProductCategoryDto.class);
        productDto.setProductCategoryDto(productCategoryDto);
        Product product = modelMapper.map(productDto, Product.class);
        when(productRepository.getById(productDto.getId())).thenReturn(product);
        productService.delete(productDto);
        verify(productCategoryRepository, times(1)).save(productCategory);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void findLast() {
        List<Product> products = new ArrayList<>();
        for (ProductDto productDto : productsDto) {
            Product product = modelMapper.map(productDto, Product.class);
            products.add(product);
        }
        when(productRepository.findAll()).thenReturn(products);
        productService.findLast();
        assertEquals(2, productService.findLast().size());
    }

    @Test
    void findByPhotoName() {
        when(productRepository.findByNamePhoto("photoName")).thenReturn(product);
        ProductDto productDto = productService.findByPhotoName("photoName");
        verify(productRepository, times(1)).findByNamePhoto("photoName");
        assertEquals("photoName", productDto.getNamePhoto());
    }

    @Test
    void findByPhotoNameFail() {
        when(productRepository.findByNamePhoto("photoName")).thenReturn(null);
        ProductDto productDto = productService.findByPhotoName("photoName");
        verify(productRepository, times(1)).findByNamePhoto("photoName");
        assertNull(productDto);
    }

    @Test
    void addPhoto() {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productService.addPhoto(productDto);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deletePhoto() {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productService.addPhoto(productDto);
        verify(productRepository, times(1)).save(product);
    }
}