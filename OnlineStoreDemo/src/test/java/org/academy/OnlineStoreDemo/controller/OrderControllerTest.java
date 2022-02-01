package org.academy.OnlineStoreDemo.controller;

import org.academy.OnlineStoreDemo.dto.*;
import org.academy.OnlineStoreDemo.model.entity.*;
import org.academy.OnlineStoreDemo.model.repository.OrderRepository;
import org.academy.OnlineStoreDemo.model.repository.UserRepository;
import org.academy.OnlineStoreDemo.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private WebApplicationContext context;

    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderProductService orderProductService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CardService cardService;

    @Mock
    private ModelMapper modelMapper;

    @MockBean
    private OrderRepository orderRepository;

    private MockMvc mockMvc;

    private OrderDto orderDto;

    private ProductDto productDto;

    private User user;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@ml.lm");
        userDto.setPhoneNumber("123456789123");
        userDto.setLogin("login");
        userDto.setPassword("password");
        user = new User();
        user.setId(1);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@ml.lm");
        user.setPhoneNumber("123456789123");
        user.setLogin("login");
        user.setPassword("password");
        Role role = new Role();
        role.setId(1);
        user.setRole(role);
        Order order = new Order();
        Order order1 = new Order();
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order1);
        StateOrder stateOrder= new StateOrder();
        stateOrder.setName("NEW");
        order.setStateOrder(stateOrder);
        order1.setStateOrder(stateOrder);
        user.setOrders(orders);
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
        CardDto cardDto = new CardDto();
        cardDto.setId(1);
        cardDto.setName("card name");
        cardDto.setNumber("1234567890000000");
        cardDto.setCvv("000");
        cardDto.setTotalAmount(10.0);
        Card card = new Card();
        card.setId(1);
        card.setName("card name");
        card.setNumber("1234567890000000");
        card.setCvv("000");
        card.setTotalAmount(10.0);
        List<CardDto> cardsDto = new ArrayList<>();
        cardsDto.add(cardDto);
        userDto.setCardsDto(cardsDto);
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        user.setCards(cards);
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void getOrdersPage() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.view().name("/orders"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "login", roles = "USER")
    void addProductToOrder() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
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
        when(userRepository.findByLogin("login")).thenReturn(user);
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
    @WithMockUser(username = "login", roles = "USER")
    void removeProductFromOrder() throws Exception {
        when(userRepository.findByLogin("login")).thenReturn(user);
        when(orderProductService.removeProductFromOrder(productDto.getId(),"login")).thenReturn(orderDto);
        mockMvc.perform(post("/orders/remove")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andDo(MockMvcResultHandlers.print());
        verify(orderProductService, times(1)).removeProductFromOrder(productDto.getId(),"login");
    }
}