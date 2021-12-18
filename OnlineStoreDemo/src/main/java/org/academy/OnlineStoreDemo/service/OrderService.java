package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.model.entity.Order;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.User;

import java.util.List;

public interface OrderService {
    //    void saveProductToOrder(User user, Product product);
    OrderDto findById(Integer id);

    OrderDto createOrderIfNotActive(UserDto userDto);

    void payOrder(OrderDto orderDto, CardDto cardDto);

    List<OrderDto> findAll();


    void setDelivered(OrderDto orderDto);
}
