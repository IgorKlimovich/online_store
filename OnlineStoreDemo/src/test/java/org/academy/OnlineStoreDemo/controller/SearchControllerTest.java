package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UtilService;
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
import java.util.Collections;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

    @Mock
    private ProductCategoryService productCategoryService;

    @Mock
    private UtilService utilService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private SearchController searchController;

    private MockMvc mockMvc;

    private ProductCategoryDto productCategoryDto;

    private ProductDto productDto;

    private List<ProductDto> productsDto;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/templates/");
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).setViewResolvers(viewResolver).build();
        productCategoryDto = new ProductCategoryDto();
        productCategoryDto.setId(1);
        productCategoryDto.setName("nameCategory");
        productDto = new ProductDto();
        productDto.setName("name");
        ProductDto productDto1 = new ProductDto();
        productDto.setId(1);
        productDto1.setId(2);
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
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attribute("notEmpty", true))
                .andExpect(MockMvcResultMatchers.view().name("search"))
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
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("noCategory", "такой категории нету"))
                .andExpect(MockMvcResultMatchers.view().name("/shop"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findAll();
        verify(productService, times(1)).findLast();
        assertEquals(2, productService.findAll().size());
    }

    @Test
    void searchHeader() throws Exception {
        when(productService.findAllByName(productDto.getName())).thenReturn(Collections.singletonList(productDto));
        mockMvc.perform(get("/search/header")
                        .param("searchHeader", productDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("notEmpty", true))
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attribute("productCategoryDto", new ProductCategoryDto()))
                .andExpect(MockMvcResultMatchers.view().name("search"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findAllByName(productDto.getName());
        assertEquals(1, productService.findAllByName(productDto.getName()).size());
    }

    @Test
    void searchHeaderFail() throws Exception {
        when(productService.findAllByName(productDto.getName())).thenReturn(EMPTY_LIST);
        mockMvc.perform(get("/search/header")
                        .param("searchHeader", productDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attribute("empty", "не найдено ни одного товара"))
                .andExpect(MockMvcResultMatchers.view().name("search"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findAllByName(productDto.getName());
        assertEquals(0, productService.findAllByName(productDto.getName()).size());
    }

    @Test
    void searchCategory() throws Exception {
        when(productCategoryService.findById(productCategoryDto.getId())).thenReturn(productCategoryDto);
        mockMvc.perform(get("/search/searchCategory/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attribute("notEmpty", true))
                .andExpect(MockMvcResultMatchers.view().name("search"))
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
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attribute("empty", "не найдено ни одного товара"))
                .andExpect(MockMvcResultMatchers.view().name("search"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findById(productCategoryDto.getId());
        assertEquals(productCategoryDto, productCategoryService.findById(productCategoryDto.getId()));
    }
}