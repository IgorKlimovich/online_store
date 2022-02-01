package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.UserService;
import org.academy.OnlineStoreDemo.valid.ProductCategoryValidatorService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class AdminProductCategoryControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Mock
    Principal principal;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductCategoryValidatorService productCategoryValidatorService;

    @MockBean
    private ProductCategoryService productCategoryService;

    private ProductCategoryDto productCategoryDto;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        productCategoryDto = new ProductCategoryDto();
        ProductCategoryDto productCategoryDto1 = new ProductCategoryDto();
        productCategoryDto.setId(1);
        productCategoryDto1.setId(2);
        productCategoryDto.setName("categoryName");
        userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@ml.lm");
        userDto.setPhoneNumber("123456789123");
        userDto.setLogin("login");
        userDto.setPassword("password");
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getAdminProductCategoryPage() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productCategoryService.findById(productCategoryDto.getId())).thenReturn(productCategoryDto);
        mockMvc.perform(get("/admin/product_category/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attribute("productCategoryDto", productCategoryDto))
                .andExpect(MockMvcResultMatchers.view().name("admin/adminProductCategory"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findById(productCategoryDto.getId());
    }

    @Test
    void getAdminProductCategoryPageFailNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admin/product_category/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", authorities = "USER")
    void getAdminProductCategoryPageFailNoAuthority() throws Exception {
        mockMvc.perform(get("/admin/product_category/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.redirectedUrl(null))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void updateProductCategory() throws Exception {
        when(productCategoryValidatorService.validateProductCategory(productCategoryDto.getName())).thenReturn("");
        when(productCategoryService.update(productCategoryDto)).thenReturn(productCategoryDto);
        mockMvc.perform(post("/admin/product_category/update")
                        .param("id", productCategoryDto.getId().toString())
                        .param("name", productCategoryDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attribute("productCategoryDto", productCategoryDto))
                .andExpect(MockMvcResultMatchers.view().name("admin/adminProductCategory"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).update(productCategoryDto);
        assertEquals(productCategoryDto,productCategoryService.update(productCategoryDto));
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void updateProductCategoryFailExistCategory() throws Exception {
        when(productCategoryValidatorService
                .validateProductCategory(productCategoryDto.getName())).thenReturn("existCategory");
        when(productCategoryService.findById(productCategoryDto.getId())).thenReturn(productCategoryDto);
        mockMvc.perform(post("/admin/product_category/update")
                        .param("id", productCategoryDto.getId().toString())
                        .param("name", productCategoryDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.model().attribute("existCategory", true))
                .andExpect(MockMvcResultMatchers.view().name("admin/adminProductCategory"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findById(productCategoryDto.getId());
        verify(productCategoryService, times(0)).update(productCategoryDto);
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void deleteProductCategory() throws Exception {
        mockMvc.perform(post("/admin/product_category/delete")
                        .param("id", productCategoryDto.getId().toString())
                        .param("name", productCategoryDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("admin/adminProductCategories"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).delete(productCategoryDto);
    }
}