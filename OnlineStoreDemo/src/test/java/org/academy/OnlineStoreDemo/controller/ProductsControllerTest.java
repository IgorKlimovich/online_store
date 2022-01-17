package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    private ProductCategoryService productCategoryService;

    @InjectMocks
    private ProductsController productsController;

    private MockMvc mockMvc;

    private ProductCategoryDto productCategoryDto;

    private List<ProductCategoryDto> productCategoriesDto;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/templates/");
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(productsController).setViewResolvers(viewResolver).build();
        productCategoryDto = new ProductCategoryDto();
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
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findAll();
        assertEquals(2, productCategoryService.findAll().size());
    }

    @Test
    void getListProductByCategory() throws Exception {
        when(productCategoryService.findByName(productCategoryDto.getName())).thenReturn(productCategoryDto);
        mockMvc.perform(get("/products/list/name"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("list"))
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findByName(productCategoryDto.getName());
        assertEquals(2, productCategoryService.findByName(productCategoryDto.getName()).getProductsDto().size());
    }
}