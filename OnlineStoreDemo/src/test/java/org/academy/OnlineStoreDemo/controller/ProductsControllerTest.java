package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
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
class ProductsControllerTest {

    @MockBean
    private ProductCategoryService productCategoryService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private List<ProductCategoryDto> productCategoriesDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();
        ProductCategoryDto productCategoryDto1 = new ProductCategoryDto();
        productCategoryDto.setId(1);
        productCategoryDto.setName("name");
        productCategoryDto1.setId(2);
        productCategoriesDto = new ArrayList<>();
        productCategoriesDto.add(productCategoryDto);
        productCategoriesDto.add(productCategoryDto1);
        ProductDto productDto = new ProductDto();
        ProductDto productDto1 = new ProductDto();
        productDto.setId(1);
        productDto1.setId(2);
        List<ProductDto> productsDto = new ArrayList<>();
        productsDto.add(productDto);
        productsDto.add(productDto1);
        productCategoryDto.setProductsDto(productsDto);
    }

    @Test
    void getProductsPage() throws Exception {
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findAll();
        assertEquals(2, productCategoryService.findAll().size());
    }
}