package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.*;
import org.academy.OnlineStoreDemo.service.OrderService;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class AdminOrderControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private Principal principal;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    private OrderDto orderDto;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        orderDto = new OrderDto();
        OrderDto orderDto1 = new OrderDto();
        orderDto.setId(1);
        orderDto1.setId(2);
        StateOrderDto stateOrderDto = new StateOrderDto();
        StateOrderDto stateOrderDto1 = new StateOrderDto();
        stateOrderDto.setId(1);
        stateOrderDto.setName("NEW");
        stateOrderDto1.setId(2);
        stateOrderDto1.setName("PAID");
        userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@ml.lm");
        userDto.setPhoneNumber("123456789123");
        userDto.setLogin("login");
        userDto.setPassword("password");
        orderDto.setStateOrderDto(stateOrderDto);
        orderDto.setUserDto(userDto);
        orderDto1.setStateOrderDto(stateOrderDto1);
        OrderProductDto orderProductDto = new OrderProductDto();
        OrderProductDto orderProductDto1 = new OrderProductDto();
        List<OrderProductDto> ordersProductsDto = new ArrayList<>();
        ordersProductsDto.add(orderProductDto);
        ordersProductsDto.add(orderProductDto1);
        orderDto.setOrderProductsDto(ordersProductsDto);
        ProductDto productDto = new ProductDto();
        ProductDto productDto1 = new ProductDto();
        productDto.setId(1);
        productDto1.setId(2);
        productDto.setName("product");
        productDto1.setName("product1");
        orderProductDto.setProductDto(productDto);
        orderProductDto1.setProductDto(productDto1);
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void getOrderPage() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(orderService.findById(orderDto.getId())).thenReturn(orderDto);
        mockMvc.perform(get("/admin/order/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("adminOrder"))
                .andExpect(MockMvcResultMatchers.model().attribute("orderDto", orderDto))
                .andDo(MockMvcResultHandlers.print());
        verify(orderService, times(1)).findById(orderDto.getId());
    }

    @Test
    void getOrderPageFailNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admin/order/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", authorities = "USER")
    void getOrderPageFailNoAuthority() throws Exception {
        mockMvc.perform(get("/admin/order/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.redirectedUrl(null))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", authorities = "ADMIN")
    void deliverOrder() throws Exception {
        when(principal.getName()).thenReturn("login");
        when(userService.findByLogin("login")).thenReturn(userDto);
        when(orderService.findById(orderDto.getId())).thenReturn(orderDto);
        mockMvc.perform(post("/admin/order/admin/order/deliver/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.view().name("adminOrder"))
                .andExpect(MockMvcResultMatchers.model().attribute("orderDto", orderDto))
                .andDo(MockMvcResultHandlers.print());
        verify(orderService, times(1)).setDelivered(orderDto);
        verify(orderService, times(2)).findById(orderDto.getId());
    }
}