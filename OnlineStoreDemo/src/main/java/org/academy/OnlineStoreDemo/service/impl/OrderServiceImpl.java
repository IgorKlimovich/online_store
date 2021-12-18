package org.academy.OnlineStoreDemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.*;
import org.academy.OnlineStoreDemo.model.repository.*;
import org.academy.OnlineStoreDemo.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShopCountRepository shopCountRepository;
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;


    public OrderServiceImpl(OrderRepository orderRepository,
                            ShopCountRepository shopCountRepository,
                            CardRepository cardRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.shopCountRepository = shopCountRepository;
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDto createOrderIfNotActive(UserDto userDto) {
        User user=modelMapper.map(userDto, User.class);
        if (user.getOrders()
                .stream()
                .noneMatch(item -> item.getStateOrder().getName().equals("NEW"))) {

            Order order = new Order();
            List<Order> orders = user.getOrders();
            order.setDate(new Date());
            order.setFullPrice(0.0);
            StateOrder stateOrder = new StateOrder();
            stateOrder.setId(1);
            stateOrder.setName("NEW");
            order.setStateOrder(stateOrder);
            order.setUser(user);
            orders.add(order);
            user.setOrders(orders);
            orderRepository.save(order);
            log.info("in create order if not exist: new order {} saved for user {}",order,user);
            return modelMapper.map(order,OrderDto.class);
        }
        Order oldOrder = null;
        try {
            oldOrder = user.getOrders()
                    .stream()
                    .filter(item -> item.getStateOrder().getName().equals("NEW"))
                    .findFirst()
                    .orElseThrow(Exception::new);
        } catch (Exception e) {
            log.warn("in create order if not exist: order by state order name 'NEW' not found");
        }
        log.info("in create order if not exist: return order {} by user {}",oldOrder,user);
        return modelMapper.map(oldOrder,OrderDto.class);
    }

    @Override
    @Transactional
    public void payOrder(OrderDto orderDto, CardDto cardDto) {
        Order order = modelMapper.map(orderDto,Order.class);
        Card card=modelMapper.map(cardDto,Card.class);
        User user = order.getUser();
        try {
            Card userCard = user.getCards()
                    .stream()
                    .filter(item -> item.getNumber().equals(card.getNumber()))
                    .findFirst().orElseThrow(Exception::new);
            userCard.setTotalAmount(userCard.getTotalAmount() - order.getFullPrice());
            StateOrder stateOrder = new StateOrder();
            stateOrder.setId(2);
            stateOrder.setName("PAID");
            order.setStateOrder(stateOrder);
            ShopCount shopCount = shopCountRepository.getById(1);
            shopCount.setTotalAmount(shopCount.getTotalAmount() + order.getFullPrice());
            shopCountRepository.save(shopCount);
            orderRepository.save(order);
            cardRepository.save(userCard);
            log.info("in pay order: order {} payed by card {} for user {}",order, card , user);
        } catch (Exception e) {
            log.warn("in pay order: card not found by number {} for user {}",card.getNumber(), user);
        }
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders=orderRepository.findAll();
        List<OrderDto> ordersDto = new ArrayList<>();
        for (Order order : orders) {
            OrderDto map = modelMapper.map(order, OrderDto.class);
            ordersDto.add(map);
        }
        log.info("in find all orders: founded {} orders", orders.size());
      return ordersDto;
    }

    @Override
    public void setDelivered(OrderDto orderDto) {
        StateOrder stateOrder = new StateOrder();
        stateOrder.setId(3);
        stateOrder.setName("DELIVERED");
        Order order = modelMapper.map(orderDto, Order.class);
        order.setStateOrder(stateOrder);
        orderRepository.save(order);
        log.info("in set delivered order: order{} state order updated to DELIVERED",order);
    }


    @Override
    public OrderDto findById(Integer id) {
        Order order = null;
        try {
            order = orderRepository.findById(id).orElseThrow(Exception::new);

        } catch (Exception e) {
            log.warn("in find by id order: order by id {} not found",id);
        }
        OrderDto orderDto = modelMapper.map(order,OrderDto.class);
        log.info("in find by id order: founded{} by id {}",order,id);
        return orderDto;
    }
}
