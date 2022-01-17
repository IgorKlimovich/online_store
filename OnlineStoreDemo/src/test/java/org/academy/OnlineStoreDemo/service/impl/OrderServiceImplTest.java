package org.academy.OnlineStoreDemo.service.impl;

import org.academy.OnlineStoreDemo.dto.*;
import org.academy.OnlineStoreDemo.mail.EmailService;
import org.academy.OnlineStoreDemo.model.entity.*;
import org.academy.OnlineStoreDemo.model.repository.CardRepository;
import org.academy.OnlineStoreDemo.model.repository.OrderRepository;
import org.academy.OnlineStoreDemo.model.repository.ShopCountRepository;
import org.academy.OnlineStoreDemo.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ShopCountRepository shopCountRepository;

    @MockBean
    private CardRepository cardRepository;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private EmailService emailService;

    private Order order;

    private List<Order> orders;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        order = new Order();
        order.setId(1);
        Order order1 = new Order();
        order1.setId(2);
        orders = new ArrayList<>();
        orders.add(order);
        orders.add(order1);
        userDto = new UserDto();
        userDto.setId(1);
        List<OrderDto> ordersDto = new ArrayList<>();
        userDto.setOrdersDto(ordersDto);
    }

    @Test
    void createOrderIfNotActive() {
        List<OrderDto> ordersDto = new ArrayList<>();
        userDto.setOrdersDto(ordersDto);
        orderService.createOrderIfNotActive(userDto);
        assertEquals("NEW", orderService.createOrderIfNotActive(userDto).getStateOrderDto().getName());
    }

    @Test
    void returnNewOrderIfExist() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        StateOrderDto stateOrderDto = new StateOrderDto();
        stateOrderDto.setId(1);
        stateOrderDto.setName("NEW");
        orderDto.setStateOrderDto(stateOrderDto);
        List<OrderDto> ordersDto = new ArrayList<>();
        ordersDto.add(orderDto);
        userDto.setOrdersDto(ordersDto);
        orderService.createOrderIfNotActive(userDto);
        assertEquals(orderDto, orderService.createOrderIfNotActive(userDto));
    }

    @Test
    void payOrder() {
        List<CardDto> cardsDto = new ArrayList<>();
        CardDto cardDto = new CardDto();
        cardDto.setId(1);
        cardDto.setName("card");
        cardDto.setCvv("222");
        cardDto.setNumber("1212121212343434");
        cardDto.setTotalAmount(1000.0);
        cardsDto.add(cardDto);
        userDto.setCardsDto(cardsDto);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setUserDto(userDto);
        orderDto.setFullPrice(200.0);
        ShopCount shopCount = new ShopCount();
        shopCount.setId(1);
        shopCount.setTotalAmount(2000.0);
        when(shopCountRepository.getById(shopCount.getId())).thenReturn(shopCount);
        Card card = modelMapper.map(cardDto, Card.class);
        orderService.payOrder(orderDto, cardDto);
        verify(shopCountRepository, times(1)).save(shopCount);
        verify(orderRepository, times(1)).save(order);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void findAll() {
        when(orderRepository.findAll()).thenReturn(orders);
        List<OrderDto> ordersDto = orderService.findAll();
        verify(orderRepository, times(1)).findAll();
        assertEquals(2, ordersDto.size());
    }

    @Test
    void setDelivered() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setFirstName("name");
        userDto.setEmail("email");
        orderDto.setUserDto(userDto);
        Order order = modelMapper.map(orderDto, Order.class);
        orderService.setDelivered(orderDto);
        verify(orderRepository, times(1)).save(order);
        verify(emailService, times(1)).sendDeliverMessage(userDto.getEmail(), userDto.getFirstName());
    }

    @Test
    void findById() {
        when(orderRepository.findById(1)).thenReturn(Optional.ofNullable(order));
        OrderDto orderDto = orderService.findById(1);
        verify(orderRepository, times(1)).findById(1);
        assertEquals(1, orderDto.getId());
    }

    @Test
    void findByIdFail() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        OrderDto orderDto = orderService.findById(1);
        verify(orderRepository, times(1)).findById(1);
        assertNull(orderDto);
    }

}