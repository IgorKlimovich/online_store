package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.*;
import org.academy.OnlineStoreDemo.service.*;
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

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
class AdminControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private Principal principal;

    @MockBean
    private UserService userService;

    @MockBean
    private UtilService utilService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductCategoryService productCategoryService;

    private UserDto userDto;

    private List<UserDto> usersDto;

    private ProductDto productDto;

    private ProductDto productDto1;

    private List<ProductDto> productsDto;

    private ProductCategoryDto productCategoryDto;

    private ProductCategoryDto productCategoryDto1;

    private List<ProductCategoryDto> productCategoriesDto;

    private List<OrderDto> ordersDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@ml.lm");
        userDto.setPhoneNumber("123456789123");
        userDto.setLogin("login");
        userDto.setPassword("password");
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1);
        roleDto.setName("USER");
        StateDto stateDto = new StateDto();
        stateDto.setId(1);
        stateDto.setName("ACTIVE");
        userDto.setStateDto(stateDto);
        userDto.setRoleDto(roleDto);
        UserDto userDto1 = new UserDto();
        usersDto = new ArrayList<>();
        usersDto.add(userDto);
        usersDto.add(userDto1);
        productDto = new ProductDto();
        productDto1 = new ProductDto();
        productDto.setId(1);
        productDto.setName("name");
        productDto.setPrice(100.0);
        productDto.setAmount(5);
        productDto.setDescription("description");
        productDto1.setId(2);
        productDto.setIsExist(true);
        productDto1.setIsExist(true);
        productsDto = new ArrayList<>();
        productsDto.add(productDto);
        productsDto.add(productDto1);
        productCategoryDto = new ProductCategoryDto();
        productCategoryDto1 = new ProductCategoryDto();
        productCategoryDto.setId(1);
        productCategoryDto1.setId(2);
        productCategoryDto.setName("categoryName");
        productCategoryDto1.setName("categoryName1");
        productDto.setProductCategoryDto(productCategoryDto);
        productDto1.setProductCategoryDto(productCategoryDto1);
        productCategoriesDto = new ArrayList<>();
        productCategoriesDto.add(productCategoryDto);
        productCategoriesDto.add(productCategoryDto1);
        OrderDto orderDto = new OrderDto();
        OrderDto orderDto1 = new OrderDto();
        orderDto.setId(1);
        orderDto1.setId(2);
        orderDto.setUserDto(userDto);
        orderDto1.setUserDto(userDto1);
        StateOrderDto stateOrderDto = new StateOrderDto();
        StateOrderDto stateOrderDto1 = new StateOrderDto();
        stateOrderDto.setName("NEW");
        stateOrderDto.setId(1);
        stateOrderDto1.setId(2);
        stateOrderDto1.setName("PAID");
        orderDto.setStateOrderDto(stateOrderDto);
        orderDto1.setStateOrderDto(stateOrderDto1);
        ordersDto = new ArrayList<>();
        ordersDto.add(orderDto);
        ordersDto.add(orderDto1);
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getAdminPage() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("admin"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAdminPageFailNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", authorities = "USER")
    void getAdminPageFaiHasNotAuthority() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.redirectedUrl(null))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void searchUser() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(utilService.findUserByParameters("login", "login")).thenReturn(userDto);
        mockMvc.perform(get("/admin/search")
                        .param("parameter", "login")
                        .param("name", "login"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("user"))
                .andDo(MockMvcResultHandlers.print());
        verify(utilService, times(2)).findUserByParameters("login", "login");
        assertEquals(userDto, utilService.findUserByParameters("login", "login"));
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void searchAllUsers() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(userService.findAll()).thenReturn(usersDto);
        mockMvc.perform(get("/admin/search")
                        .param("parameter", "all")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("users"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).findAll();
        assertEquals(2, userService.findAll().size());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void searchUserFail() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(utilService.findUserByParameters("login", "login")).thenReturn(null);
        mockMvc.perform(get("/admin/search")
                        .param("parameter", "login")
                        .param("name", "login"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attribute("userNotFound", true))
                .andExpect(MockMvcResultMatchers.view().name("admin"))
                .andDo(MockMvcResultHandlers.print());
        verify(utilService, times(1)).findUserByParameters("login", "login");
        assertNull(utilService.findUserByParameters("login", "login"));
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getUsersPage() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(userService.findAll()).thenReturn(usersDto);
        when(utilService.sortUsersByParameters(usersDto, "login")).thenReturn(usersDto);
        mockMvc.perform(get("/admin/users")
                        .param("parameter", "login"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("users"))
                .andDo(MockMvcResultHandlers.print());
        verify(utilService, times(1)).sortUsersByParameters(usersDto, "login");
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getUserPage() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(userService.findById(userDto.getId())).thenReturn(userDto);
        mockMvc.perform(get("/admin/user/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("user"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(2)).findById(userDto.getId());
        assertEquals(userDto, userService.findById(userDto.getId()));
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void ban() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(userService.findById(userDto.getId())).thenReturn(userDto);
        mockMvc.perform(post("/admin/user/1/ban"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/user/1"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).toBan(userDto.getId());
        verify(userService, times(1)).findById(userDto.getId());
        assertEquals(userDto, userService.findById(userDto.getId()));

    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void unBan() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(userService.findById(userDto.getId())).thenReturn(userDto);
        mockMvc.perform(post("/admin/user/1/unban"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/user/1"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).unBan(userDto.getId());
        verify(userService, times(1)).findById(userDto.getId());
        assertEquals(userDto, userService.findById(userDto.getId()));
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void searchProductsByName() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findAllByName("name")).thenReturn(Collections.singletonList(productDto));
        mockMvc.perform(get("/admin/searchProducts")
                        .param("parameterProd", "nameProd")
                        .param("nameProd", "name"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("adminProducts"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findAllByName("name");
        assertEquals(1, productService.findAllByName("name").size());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void searchAllProducts() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findAll()).thenReturn(productsDto);
        mockMvc.perform(get("/admin/searchProducts")
                        .param("parameterProd", "allProd")
                        .param("nameProd", ""))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("adminProducts"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findAll();
        verify(productCategoryService, times(1)).findAll();
        assertEquals(2, productService.findAll().size());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void searchProductsByNameFail() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findAllByName("name")).thenReturn(Collections.EMPTY_LIST);
        mockMvc.perform(get("/admin/searchProducts")
                        .param("parameterProd", "nameProd")
                        .param("nameProd", "name"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("productNotFound", true))
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("admin"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findAllByName("name");
        assertEquals(0, productService.findAllByName("name").size());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getSortedProductsPage() throws Exception {
        String[] ids = {productDto.getId().toString(), productDto1.getId().toString()};
        List<String> idProduct = Arrays.asList(ids);
        List<Integer> integers = idProduct.stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findAllByIds(integers)).thenReturn(productsDto);
        mockMvc.perform(post("/admin/sortProducts")
                        .param("parameter", "name")
                        .param("ids", ids))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("adminProducts"))
                .andDo(MockMvcResultHandlers.print());
        verify(utilService, times(1)).sortProductByParameters(productsDto, "name");
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void saveCategory() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(post("/admin/saveCategory")
                        .param("addCategory", "categoryName"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("adminProductCategories"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findAll();
        verify(productCategoryService, times(1)).save("categoryName");
        assertEquals(2, productCategoryService.findAll().size());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void saveCategoryFailEmptyName() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(post("/admin/saveCategory")
                        .param("addCategory", ""))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("emptyCategoryName", true))
                .andExpect(MockMvcResultMatchers.view().name("adminProductCategories"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findAll();
        assertEquals(2, productCategoryService.findAll().size());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void saveCategoryFailExistProductCategory() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productCategoryService.existsProductCategoryByName("categoryName")).thenReturn(true);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(post("/admin/saveCategory")
                        .param("addCategory", "categoryName"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attribute("existCategory", true))
                .andExpect(MockMvcResultMatchers.view().name("adminProductCategories"))
                .andDo(MockMvcResultHandlers.print());
        verify(productCategoryService, times(1)).findAll();
        assertEquals(2, productCategoryService.findAll().size());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getProductCategoriesPageAll() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        mockMvc.perform(get("/admin/searchProductCategories")
                        .param("parameterProductCategory", "allCategories")
                        .param("nameProdCat", ""))
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attribute("productCategoriesDto", productCategoryService.findAll()))
                .andExpect(MockMvcResultMatchers.view().name("adminProductCategories"))
                .andDo(MockMvcResultHandlers.print());
        assertEquals(2, productCategoryService.findAll().size());
        verify(productCategoryService, times(3)).findAll();
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getProductCategoriesPageByName() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productCategoryService.findByName("categoryName")).thenReturn(productCategoryDto);
        mockMvc.perform(get("/admin/searchProductCategories")
                        .param("parameterProductCategory", "categoryName")
                        .param("nameProdCat", "categoryName"))
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("adminProductCategory"))
                .andDo(MockMvcResultHandlers.print());
        assertEquals(productCategoryDto, productCategoryService.findByName("categoryName"));
        verify(productCategoryService, times(2)).findByName("categoryName");
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getProductCategoriesPageByNameFail() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productCategoryService.findByName("categoryName")).thenReturn(null);
        mockMvc.perform(get("/admin/searchProductCategories")
                        .param("parameterProductCategory", "categoryName")
                        .param("nameProdCat", "categoryName"))
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("admin"))
                .andDo(MockMvcResultHandlers.print());
        assertNull(productCategoryService.findByName("categoryName"));
        verify(productCategoryService, times(2)).findByName("categoryName");
    }


    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void sortProductCategories() throws Exception {
        String[] ids = {productCategoryDto.getId().toString(), productCategoryDto1.getId().toString()};
        List<String> idCategory = Arrays.asList(ids);
        List<Integer> integers = idCategory.stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productCategoryService.findAllByIds(integers)).thenReturn(productCategoriesDto);
        mockMvc.perform(post("/admin/sortProductCategories")
                        .param("parameterCategory", "categoryName")
                        .param("idsCategory", ids))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("adminProductCategories"))
                .andDo(MockMvcResultHandlers.print());
        verify(utilService, times(1))
                .sortProductCategoriesByParameters(productCategoriesDto, "categoryName");
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void saveProduct() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        productDto.setId(null);
        productDto.setNamePhoto("hello.txt");
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        when(productService.findByPhotoName("hello.txt")).thenReturn(productDto);
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        when(productService.findAll()).thenReturn(productsDto);
        when(productCategoryService.existsProductCategoryByName(productCategoryDto.getName())).thenReturn(true);
        when(productService.existsProductByName(productDto.getName().trim())).thenReturn(false);
        mockMvc.perform(multipart("/admin/saveProduct").file(file)
                        .param("name", productDto.getName())
                        .param("amount", productDto.getAmount().toString())
                        .param("description", productDto.getDescription())
                        .param("price", productDto.getPrice().toString())
                        .param("isExist", productDto.getIsExist().toString())
                        .param("category", productCategoryDto.getName())
                        .param("file", "file"))
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("adminProducts"))
                .andDo(MockMvcResultHandlers.print());
        //   verify(productService, times(1)).saveWithCategoryName(productDto, productCategoryDto.getName());
        verify(productService, times(1)).findByPhotoName(productDto.getNamePhoto());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void saveProductFailEmptyName() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        productDto.setId(null);
        productDto.setNamePhoto("hello.txt");
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        when(productCategoryService.findAll()).thenReturn(productCategoriesDto);
        when(productService.findAll()).thenReturn(productsDto);
        mockMvc.perform(multipart("/admin/saveProduct").file(file)
                        .param("name", "")
                        .param("amount", productDto.getAmount().toString())
                        .param("description", productDto.getDescription())
                        .param("price", productDto.getPrice().toString())
                        .param("category", productCategoryDto.getName())
                        .param("file", "file"))
                .andExpect(MockMvcResultMatchers.model().size(6))
                .andExpect(MockMvcResultMatchers.model().attribute("emptyProductName", true))
                .andExpect(MockMvcResultMatchers.view().name("adminProducts"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findAll();
        verify(productCategoryService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getAdminOrdersPage() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(orderService.findAll()).thenReturn(ordersDto);
        mockMvc.perform(get("/admin/searchOrders")
                        .param("parameterOrder", "all"))
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("adminOrders"))
                .andDo(MockMvcResultHandlers.print());
        verify(orderService, times(1)).findAll();
        assertEquals(2, orderService.findAll().size());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getAdminOrdersPageNew() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(orderService.findAll()).thenReturn(ordersDto);
        mockMvc.perform(get("/admin/searchOrders")
                        .param("parameterOrder", "new"))
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("adminOrders"))
                .andDo(MockMvcResultHandlers.print());
        verify(orderService, times(1)).findAll();
    }
}