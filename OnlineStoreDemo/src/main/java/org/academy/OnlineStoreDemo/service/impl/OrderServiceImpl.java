package org.academy.OnlineStoreDemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.exception.CardNotFoundException;
import org.academy.OnlineStoreDemo.exception.OrderNotFoundException;
import org.academy.OnlineStoreDemo.mail.EmailService;
import org.academy.OnlineStoreDemo.model.entity.*;
import org.academy.OnlineStoreDemo.model.repository.*;
import org.academy.OnlineStoreDemo.service.OrderService;
import org.academy.OnlineStoreDemo.util.UtilListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final UtilListMapper utilListMapper;
    private final CardRepository cardRepository;
    private final OrderRepository orderRepository;
    private final ShopCountRepository shopCountRepository;

    @Override
    @Transactional
    public OrderDto createOrderIfNotActive(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        if (user.getOrders()
                .stream()
                .noneMatch(item -> item.getStateOrder().getName().equals("NEW"))) {
            Order order = new Order();
            List<Order> orders = user.getOrders();
            order.setDate(new Date());
            order.setFullPrice(0.0);
            order.setStateOrder(new StateOrder(1,"NEW"));
            order.setUser(user);
            orders.add(order);
            user.setOrders(orders);
            orderRepository.save(order);
            log.info("in create order if not exist: new order {} saved for user {}", order, user);
            return modelMapper.map(order, OrderDto.class);
        }
        Order oldOrder = user.getOrders()
                .stream()
                .filter(item -> item.getStateOrder().getName().equals("NEW"))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("order not found"));
        log.info("in create order if not exist: return order {} by user {}", oldOrder, user);
        return modelMapper.map(oldOrder, OrderDto.class);
    }

    @Override
    @Transactional
    public void payOrder(OrderDto orderDto, CardDto cardDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        Card card = modelMapper.map(cardDto, Card.class);
        User user = order.getUser();
        Card userCard = user.getCards()
                .stream()
                .filter(item -> item.getNumber().equals(card.getNumber()))
                .findFirst().orElseThrow(() -> new CardNotFoundException("card not found"));
        userCard.setTotalAmount(userCard.getTotalAmount() - order.getFullPrice());
        order.setStateOrder(new StateOrder(2,"PAID"));
        ShopCount shopCount = shopCountRepository.getById(1);
        shopCount.setTotalAmount(shopCount.getTotalAmount() + order.getFullPrice());
        shopCountRepository.save(shopCount);
        orderRepository.save(order);
        cardRepository.save(userCard);
        log.info("in pay order: order {} payed by card {} for user {}", order, card, user);
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        log.info("in find all orders: founded {} orders", orders.size());
        return utilListMapper.mapList(orders,OrderDto.class);
    }

    @Override
    @Transactional
    public OrderDto setDelivered(Integer id) {
        OrderDto orderDto = findById(id);
        Order order = modelMapper.map(orderDto, Order.class);
        order.setStateOrder(new StateOrder(3, "DELIVERED"));
        UserDto userDto = orderDto.getUserDto();
        emailService.sendDeliverMessage(userDto.getEmail(), userDto.getFirstName());
        log.info("in set delivered order: order{} state order updated to DELIVERED", order);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Override
    public OrderDto findById(Integer id) {
        Order order= orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException("order not found"));
        log.info("in find by id order: founded{} by id {}", order, id);
        return modelMapper.map(order, OrderDto.class);
    }
}
