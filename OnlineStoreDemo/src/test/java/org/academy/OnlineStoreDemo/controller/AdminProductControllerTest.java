package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class AdminProductControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Mock
    private Principal principal;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductCategoryService productCategoryService;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    private ProductDto productDto;

    private ProductCategoryDto productCategoryDto;

    private List<ProductCategoryDto> productCategoriesDto;

    private List<ProductDto> productsDto;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        productDto = new ProductDto();
        productDto.setId(1);
        productDto.setName("name");
        productDto.setAmount(5);
        productDto.setDescription("description");
        productDto.setPrice(100.0);
        productDto.setIsExist(true);
        ProductDto productDto1 = new ProductDto();
        productDto1.setId(2);
        productDto1.setIsExist(true);
        productsDto = new ArrayList<>();
        productsDto.add(productDto);
        productsDto.add(productDto1);
        productCategoryDto = new ProductCategoryDto();
        ProductCategoryDto productCategoryDto1 = new ProductCategoryDto();
        productCategoryDto.setId(1);
        productCategoryDto.setName("categoryName");
        productCategoryDto1.setId(2);
        productCategoryDto1.setName("categoryName1");
        productCategoriesDto = new ArrayList<>();
        productCategoriesDto.add(productCategoryDto);
        productCategoriesDto.add(productCategoryDto1);
        productDto.setProductCategoryDto(productCategoryDto);
        productDto1.setProductCategoryDto(productCategoryDto1);
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
    void getAdminProductPage() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(1)).thenReturn(productDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(get("/admin/product/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("adminProduct"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findById(1);
        verify(productCategoryService, times(1)).findAll();
        assertEquals(productDto, productService.findById(productDto.getId()));
        assertEquals(2, productCategoryService.findAll().size());
    }

    @Test
    void getAdminProductPageFailNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admin/product/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", authorities = "USER")
    void getAdminProductPageFailHasNotAuthority() throws Exception {
        mockMvc.perform(get("/admin/product/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.redirectedUrl(null))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void updateProduct() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        when(productCategoryService.existsProductCategoryByName(productDto.getProductCategoryDto().getName()))
                .thenReturn(true);
        mockMvc.perform(post("/admin/product/update")
                        .param("id", productDto.getId().toString())
                        .param("name", productDto.getName())
                        .param("amount", productDto.getAmount().toString())
                        .param("description", productDto.getDescription())
                        .param("isExist", productDto.getIsExist().toString())
                        .param("price", productDto.getPrice().toString())
                        .param("productCategoryDto.id", productCategoryDto.getId().toString())
                        .param("productCategoryDto.name", productCategoryDto.getName()))
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("adminProduct"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).update(productDto);
        verify(productCategoryService, times(1)).findAll();
        verify(productService, times(1)).findById(productDto.getId());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void updateProductFailName() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(post("/admin/product/update")
                        .param("id", productDto.getId().toString())
                        .param("name", "")
                        .param("amount", productDto.getAmount().toString())
                        .param("description", productDto.getDescription())
                        .param("price", productDto.getPrice().toString())
                        .param("productCategoryDto.id", productCategoryDto.getId().toString())
                        .param("productCategoryDto.name", productCategoryDto.getName()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("adminProduct"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findAll();
        verify(productService, times(1)).findById(productDto.getId());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void updateProductFailPrice() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(post("/admin/product/update")
                        .param("id", productDto.getId().toString())
                        .param("name", productDto.getName())
                        .param("amount", productDto.getAmount().toString())
                        .param("description", productDto.getDescription())
                        .param("price", (String) null)
                        .param("productCategoryDto.id", productCategoryDto.getId().toString())
                        .param("productCategoryDto.name", productCategoryDto.getName()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("adminProduct"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findAll();
        verify(productService, times(1)).findById(productDto.getId());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void updateProductFailAmount() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(post("/admin/product/update")
                        .param("id", productDto.getId().toString())
                        .param("name", productDto.getName())
                        .param("amount", (String) null)
                        .param("description", productDto.getDescription())
                        .param("price", productDto.getPrice().toString())
                        .param("productCategoryDto.id", productCategoryDto.getId().toString())
                        .param("productCategoryDto.name", productCategoryDto.getName()))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("adminProduct"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findAll();
        verify(productService, times(1)).findById(productDto.getId());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void updateProductFailProductCategory() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        when(productCategoryService.existsProductCategoryByName(productDto.getProductCategoryDto().getName()))
                .thenReturn(false);
        mockMvc.perform(post("/admin/product/update")
                        .param("id", productDto.getId().toString())
                        .param("name", productDto.getName())
                        .param("amount", productDto.getAmount().toString())
                        .param("description", productDto.getDescription())
                        .param("price", productDto.getPrice().toString())
                        .param("productCategoryDto.id", productCategoryDto.getId().toString())
                        .param("productCategoryDto.name", productCategoryDto.getName()))
                .andExpect(MockMvcResultMatchers.model().attribute("categoryNotExist", true))
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("adminProduct"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findAll();
        verify(productService, times(1)).findById(productDto.getId());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void deleteProduct() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findAll()).thenReturn(productsDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(post("/admin/product/delete")
                        .param("id", productDto.getId().toString())
                        .param("name", productDto.getName())
                        .param("amount", productDto.getAmount().toString())
                        .param("isExist", productDto.getIsExist().toString())
                        .param("description", productDto.getDescription())
                        .param("price", productDto.getPrice().toString())
                        .param("productCategoryDto.id", productCategoryDto.getId().toString())
                        .param("productCategoryDto.name", productCategoryDto.getName()))
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("adminProducts"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).delete(productDto);
        verify(productService, times(1)).findAll();
        verify(productCategoryService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void addPhoto() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        mockMvc.perform(multipart("/admin/product/add_photo").file(file)
                        .param("id", productDto.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("adminProduct"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).addPhoto(productDto);
        verify(productService, times(1)).findById(productDto.getId());
        verify(productCategoryService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void deletePhotoProduct() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(post("/admin/product/delete_photo")
                        .param("id", productDto.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("adminProduct"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).deletePhoto(productDto);
        verify(productService, times(1)).findById(productDto.getId());
        verify(productCategoryService, times(1)).findAll();
    }
}