package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.model.entity.ProductCategory;
import org.academy.OnlineStoreDemo.model.repository.ProductCategoryRepository;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductCategoryServiceImplTest {

    private ProductCategory productCategory;

    private List<ProductCategory> productCategories;

    private ProductCategoryDto productCategoryDto;

    @Autowired
    private ProductCategoryService productCategoryService;

    @MockBean
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        productCategoryDto = new ProductCategoryDto();
        productCategoryDto.setId(1);
        productCategoryDto.setName("name");
        productCategoryDto.setAmount(4);
        productCategory = new ProductCategory();
        ProductCategory productCategory1 = new ProductCategory();
        productCategory.setId(1);
        productCategory.setName("name");
        productCategory.setAmount(0);
        productCategory1.setId(2);
        productCategories = new ArrayList<>();
        productCategories.add(productCategory);
        productCategories.add(productCategory1);
    }

    @Test
    void findAll() {
        when(productCategoryRepository.findAll()).thenReturn(productCategories);
        List<ProductCategoryDto> productCategoriesDto = productCategoryService.findAll();
        verify(productCategoryRepository, times(1)).findAll();
        assertEquals(2, productCategoriesDto.size());
        assertEquals(1, productCategoriesDto.get(0).getId());
    }

    @Test
    void findByName() {
        when(productCategoryRepository.findByName("name")).thenReturn(productCategory);
        ProductCategoryDto productCategoryDto = productCategoryService.findByName("name");
        verify(productCategoryRepository, times(1)).findByName("name");
        assertEquals("name", productCategoryDto.getName());
    }

    @Test
    void findByNameFail() {
        when(productCategoryRepository.findByName("name")).thenReturn(null);
        ProductCategoryDto productCategoryDto = productCategoryService.findByName("name");
        verify(productCategoryRepository, times(1)).findByName("name");
        assertNull(productCategoryDto);
    }

    @Test
    void existsProductCategoryByName() {
        when(productCategoryRepository.existsProductCategoryByName("name")).thenReturn(true);
        Boolean exist = productCategoryService.existsProductCategoryByName("name");
        verify(productCategoryRepository, times(1)).existsProductCategoryByName("name");
        assertTrue(exist);
    }

    @Test
    void existProductCategoryByNameFail() {
        when(productCategoryRepository.existsProductCategoryByName("name")).thenReturn(false);
        Boolean notExist = productCategoryService.existsProductCategoryByName("name");
        verify(productCategoryRepository, times(1)).existsProductCategoryByName("name");
        assertFalse(notExist);
    }

    @Test
    void findAllByIds() {
        List<Integer> ids = new ArrayList<>();
        ids.add(productCategory.getId());
        ids.add(3);
        productCategoryService.findAllByIds(ids);
        when(productCategoryRepository.findAll()).thenReturn(productCategories);
        assertEquals(1, productCategoryService.findAllByIds(ids).size());
    }

    @Test
    void findById() {
        when(productCategoryRepository.findById(1)).thenReturn(Optional.ofNullable(productCategory));
        ProductCategoryDto productCategoryDto = productCategoryService.findById(1);
        verify(productCategoryRepository, times(1)).findById(1);
        assertEquals(1, productCategoryDto.getId());
    }

    @Test
    void findByIdFail() {
        when(productCategoryRepository.findById(1)).thenReturn(Optional.empty());
        ProductCategoryDto productCategoryDto = productCategoryService.findById(1);
        verify(productCategoryRepository, times(1)).findById(1);
        assertNull(productCategoryDto);
    }

    @Test
    void update() {
        ProductCategory productCategory = modelMapper.map(productCategoryDto, ProductCategory.class);
        when(productCategoryRepository.findById(productCategory.getId())).thenReturn(Optional.ofNullable(productCategory));
        productCategoryService.update(productCategoryDto);
        verify(productCategoryRepository, times(1)).save(productCategory);
    }

    @Test
    void delete() {
        ProductCategory productCategory = modelMapper.map(productCategoryDto, ProductCategory.class);
        productCategoryService.delete(productCategoryDto);
        verify(productCategoryRepository, times(1)).delete(productCategory);
    }
}