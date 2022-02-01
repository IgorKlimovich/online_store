package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class SearchControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private ProductCategoryService productCategoryService;

    @MockBean
    private UtilService utilService;

    @MockBean
    private ProductService productService;

    private ProductCategoryDto productCategoryDto;

    private ProductDto productDto;

    private List<ProductDto> productsDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        productCategoryDto = new ProductCategoryDto();
        productCategoryDto.setId(1);
        productCategoryDto.setName("nameCategory");
        productDto = new ProductDto();
        productDto.setName("name");
        productDto.setPrice(100.0);
        productDto.setAmount(4);
        productDto.setIsExist(true);
        productDto.setProductCategoryDto(productCategoryDto);
        ProductDto productDto1 = new ProductDto();
        productDto.setId(1);
        productDto1.setId(2);
        productDto1.setName("product");
        productDto1.setPrice(50.0);
        productDto1.setAmount(2);
        productDto1.setIsExist(true);
        productDto1.setProductCategoryDto(productCategoryDto);
        productsDto = new ArrayList<>();
        productsDto.add(productDto);
        productsDto.add(productDto1);
        productCategoryDto.setProductsDto(productsDto);
    }

    @Test
    void searchProduct() throws Exception {
        when(productCategoryService.existsProductCategoryByName(productCategoryDto.getName())).thenReturn(true);
        when(productCategoryService.findByName(productCategoryDto.getName())).thenReturn(productCategoryDto);
        when(utilService
                .findBySearchParameters(productCategoryDto.getName(), productDto.getName(), "0", "1000"))
                .thenReturn(productsDto);
        mockMvc.perform(get("/search")
                        .param("category", productCategoryDto.getName())
                        .param("name", productDto.getName())
                        .param("minPrice", "0")
                        .param("maxPrice", "1000"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("notEmpty", true))
                .andExpect(MockMvcResultMatchers.view().name("shop/search"))
                .andDo(MockMvcResultHandlers.print());
        verify(utilService, times(1))
                .findBySearchParameters(productCategoryDto.getName(), productDto.getName(), "0", "1000");
        verify(productCategoryService, times(1)).findByName(productCategoryDto.getName());
    }

    @Test
    void searchProductFailExistCategory() throws Exception {
        when(productCategoryService.existsProductCategoryByName(productCategoryDto.getName())).thenReturn(false);
        when(productService.findAll()).thenReturn(productsDto);
        when(productService.findLast()).thenReturn(productsDto);
        mockMvc.perform(get("/search")
                        .param("category", productCategoryDto.getName())
                        .param("name", productDto.getName())
                        .param("minPrice", "0")
                        .param("maxPrice", "1000"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.model().attribute("noCategory", "такой категории нету"))
                .andExpect(MockMvcResultMatchers.view().name("shop/shop"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findAll();
        verify(productService, times(1)).findLast();
        assertEquals(2, productService.findAll().size());
    }

    @Test
    void searchHeader() throws Exception {
        when(productService.findAll()).thenReturn(productsDto);
        when(utilService.headerSearch(productDto.getName())).thenReturn(productsDto);
        mockMvc.perform(get("/search/header")
                        .param("searchHeader", productDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("notEmpty", "не найдено ни одного товара"))
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("productCategoryDto", new ProductCategoryDto()))
                .andExpect(MockMvcResultMatchers.view().name("shop/search"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void searchHeaderFail() throws Exception {
        when(productService.findAll()).thenReturn(productsDto);
        mockMvc.perform(get("/search/header")
                        .param("searchHeader", "productName"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("empty", "не найдено ни одного товара"))
                .andExpect(MockMvcResultMatchers.view().name("shop/search"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void searchCategory() throws Exception {
        when(productCategoryService.findById(productCategoryDto.getId())).thenReturn(productCategoryDto);
        mockMvc.perform(get("/search/searchCategory/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("notEmpty", "не найдено ни одного товара"))
                .andExpect(MockMvcResultMatchers.view().name("shop/search"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findById(productCategoryDto.getId());
        assertEquals(productCategoryDto, productCategoryService.findById(productCategoryDto.getId()));
    }

    @Test
    void searchCategoryFail() throws Exception {
        productCategoryDto.setProductsDto(new ArrayList<>());
        when(productCategoryService.findById(productCategoryDto.getId())).thenReturn(productCategoryDto);
        mockMvc.perform(get("/search/searchCategory/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("empty", "не найдено ни одного товара"))
                .andExpect(MockMvcResultMatchers.view().name("shop/search"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findById(productCategoryDto.getId());
        assertEquals(productCategoryDto, productCategoryService.findById(productCategoryDto.getId()));
    }
}