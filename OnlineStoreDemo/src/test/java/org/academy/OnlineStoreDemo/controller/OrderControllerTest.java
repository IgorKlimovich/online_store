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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderControllerTest {

    @Mock
    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderProductService orderProductService;

    @MockBean
    private CardService cardService;

    private MockMvc mockMvc;

    private UserDto userDto;

    private OrderDto orderDto;

    private ProductDto productDto;

    private CardDto cardDto;

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
        orderDto = new OrderDto();
        OrderDto orderDto1 = new OrderDto();
        orderDto.setId(1);
        orderDto1.setId(2);
        StateOrderDto stateOrderDto = new StateOrderDto();
        StateOrderDto stateOrderDto1 = new StateOrderDto();
        stateOrderDto.setId(1);
        stateOrderDto1.setId(2);
        stateOrderDto.setName("NEW");
        orderDto.setFullPrice(1000.0);
        stateOrderDto1.setName("PAID");
        orderDto.setStateOrderDto(stateOrderDto);
        orderDto1.setStateOrderDto(stateOrderDto1);
        List<OrderDto> ordersDto = new ArrayList<>();
        ordersDto.add(orderDto);
        ordersDto.add(orderDto1);
        productDto = new ProductDto();
        ProductDto productDto1 = new ProductDto();
        productDto.setId(1);
        productDto.setName("product name");
        productDto1.setId(2);
        productDto1.setName("product name1");
        productDto.setIsExist(true);
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();
        productCategoryDto.setId(1);
        productCategoryDto.setName("category");
        productDto.setProductCategoryDto(productCategoryDto);
        OrderProductDto orderProductDto = new OrderProductDto();
        OrderProductDto orderProductDto1 = new OrderProductDto();
        orderProductDto.setProductDto(productDto);
        orderProductDto1.setProductDto(productDto1);
        List<OrderProductDto> orderProductsDto = new ArrayList<>();
        orderProductsDto.add(orderProductDto);
        orderProductsDto.add(orderProductDto1);
        orderDto.setOrderProductsDto(orderProductsDto);
        userDto.setOrdersDto(ordersDto);
        orderDto.setUserDto(userDto);
        orderDto1.setUserDto(userDto);
        cardDto = new CardDto();
        cardDto.setId(1);
        cardDto.setName("card name");
        cardDto.setNumber("1234567890000000");
        cardDto.setCvv("000");
        cardDto.setTotalAmount(10.0);
        List<CardDto> cardsDto = new ArrayList<>();
        cardsDto.add(cardDto);
        userDto.setCardsDto(cardsDto);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void getOrdersPage() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("/orders"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getOrdersPageRedirectLogin() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void addProductToOrder() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        mockMvc.perform(post("/orders/add")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("product"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(2)).findById(productDto.getId());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void addProductToOrderNotExistProduct() throws Exception {
        productDto.setIsExist(false);
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        mockMvc.perform(post("/orders/add")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("product"))
                .andDo(MockMvcResultHandlers.print());
        verify(productService, times(1)).findById(productDto.getId());
    }

    @Test
    void addProductToOrderRedirectToLogin() throws Exception {
        mockMvc.perform(post("/orders/add")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void removeProductFromOrder() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(productService.findById(productDto.getId())).thenReturn(productDto);
        when(orderService.createOrderIfNotActive(userDto)).thenReturn(orderDto);
        mockMvc.perform(post("/orders/remove")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andDo(MockMvcResultHandlers.print());
        verify(orderProductService, times(1)).removeProductFromOrder(orderDto, productDto);
        verify(orderService, times(3)).createOrderIfNotActive(userDto);
    }

    @Test
    void removeProductFromOrderRedirectToShop() throws Exception {
        mockMvc.perform(post("/orders/remove")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/shop"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void showProductsOrder() throws Exception {
        when(userService.findByLogin(userDto.getLogin())).thenReturn(userDto);
        when(orderService.findById(orderDto.getId())).thenReturn(orderDto);
        mockMvc.perform(get("/orders/show_products")
                        .param("id", orderDto.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andDo(MockMvcResultHandlers.print());
        verify(orderService, times(1)).findById(orderDto.getId());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void showProductsOrderRedirectToShopPage() throws Exception {
        when(userService.findByLogin(userDto.getLogin())).thenReturn(userDto);
        when(orderService.findById(orderDto.getId())).thenReturn(orderDto);
        mockMvc.perform(get("/orders/show_products")
                        .param("id", "6"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/shop"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void getPayForm() throws Exception {
        when(userService.findByLogin(userDto.getLogin())).thenReturn(userDto);
        when(orderService.findById(orderDto.getId())).thenReturn(orderDto);
        mockMvc.perform(post("/orders/pay")
                        .param("id", orderDto.getId().toString())
                        .param("fullPrice", orderDto.getFullPrice().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(6))
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void saveCard() throws Exception {
        cardDto.setId(null);
        cardDto.setTotalAmount(null);
        when(userService.findByLogin(userDto.getLogin())).thenReturn(userDto);
        when(orderService.createOrderIfNotActive(userDto)).thenReturn(orderDto);
        mockMvc.perform(post("/orders/save_card")
                        .param("name", cardDto.getName())
                        .param("number", cardDto.getNumber())
                        .param("cvv", cardDto.getCvv()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andDo(MockMvcResultHandlers.print());
        verify(cardService, times(1)).save(cardDto, userDto);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void saveCardFailBindingResultErrors() throws Exception {
        when(userService.findByLogin(userDto.getLogin())).thenReturn(userDto);
        when(orderService.createOrderIfNotActive(userDto)).thenReturn(orderDto);
        mockMvc.perform(post("/orders/save_card")
                        .param("name", "maestro")
                        .param("number", "12")
                        .param("cvv", "349"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.model().errorCount(1))
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void payOrderCardFailNoMoney() throws Exception {
        when(userService.findByLogin(userDto.getLogin())).thenReturn(userDto);
        when(orderService.createOrderIfNotActive(userDto)).thenReturn(orderDto);
        mockMvc.perform(post("/orders/pay_order")
                        .param("number", cardDto.getNumber()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(6))
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", 7))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void payOrderCardFailNotExistCard() throws Exception {
        when(userService.findByLogin(userDto.getLogin())).thenReturn(userDto);
        when(orderService.createOrderIfNotActive(userDto)).thenReturn(orderDto);
        mockMvc.perform(post("/orders/pay_order")
                        .param("number", "745754735737"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(7))
                .andExpect(MockMvcResultMatchers.model().attribute("error", 6))
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void payOrderCard() throws Exception {
        cardDto.setTotalAmount(10000.0);
        when(userService.findByLogin(userDto.getLogin())).thenReturn(userDto);
        when(orderService.createOrderIfNotActive(userDto)).thenReturn(orderDto);
        when(orderService.findById(orderDto.getId())).thenReturn(orderDto);
        mockMvc.perform(post("/orders/pay_order")
                        .param("number", cardDto.getNumber()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andDo(MockMvcResultHandlers.print());
        cardDto.setId(null);
        cardDto.setName(null);
        cardDto.setTotalAmount(null);
        cardDto.setCvv(null);
        verify(orderService, times(1)).payOrder(orderDto, cardDto);
    }
}