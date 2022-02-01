package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class ShopControllerTest {

    @Mock
    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductCategoryService productCategoryService;

    private ProductDto productDto;

    private List<ProductCategoryDto> productCategoriesDto;

    private List<ProductDto> productsDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        productDto = new ProductDto();
        ProductDto productDto1 = new ProductDto();
        productDto.setId(1);
        productDto.setIsExist(true);
        productDto1.setId(2);
        productsDto = new ArrayList<>();
        productsDto.add(productDto);
        productsDto.add(productDto1);
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();
        productCategoryDto.setName("product category");
        ProductCategoryDto productCategoryDto1 = new ProductCategoryDto();
        productCategoryDto1.setName("product category1");
        productCategoriesDto = new ArrayList<>();
        productCategoriesDto.add(productCategoryDto);
        productCategoriesDto.add(productCategoryDto1);
        productDto.setProductCategoryDto(productCategoryDto);
        productDto1.setProductCategoryDto(productCategoryDto1);
    }

    @Test
    void getShopPage() throws Exception {
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        when(productService.findAll()).thenReturn(productsDto);
        when(productService.findLast()).thenReturn(productsDto);
        mockMvc.perform(get("/shop"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("shop/shop"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findAll();
        verify(productCategoryService, times(1)).findAll();
        verify(productService, times(1)).findLast();
        assertEquals(2, productService.findAll().size());
    }

    @Test
    void getOne() throws Exception {
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        mockMvc.perform(get("/shop/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("product/product"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findById(productDto.getId());
        assertEquals(productDto, productService.findById(productDto.getId()));
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void deleteProfile() throws Exception {
        when(principal.getName()).thenReturn("login");
        mockMvc.perform(post("/shop/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/shop"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).setDelete("login");
    }

    @Test
    void restore() throws Exception {
        mockMvc.perform(post("/shop/restore")
                        .param("login", "login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).setActive("login");
    }
}