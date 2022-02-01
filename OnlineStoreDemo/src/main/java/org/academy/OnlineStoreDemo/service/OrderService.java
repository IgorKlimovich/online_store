package org.academy.OnlineStoreDemo.service;

import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.exception.CardNotFoundException;
import org.academy.OnlineStoreDemo.exception.OrderNotFoundException;

import java.util.List;

public interface OrderService {

    /**
     * Returns the order from database by id
     * @param id id order for search
     * @return order by id
     * @throws OrderNotFoundException if there is no order with the id in database
     */
    OrderDto findById(Integer id);

    /**
     * Returns the old order with a 'NEW' state, if it does not exist, creates a new one
     * @param userDto user for whom the order is being created
     * @return the order with 'NEW' status
     */
    OrderDto createOrderIfNotActive(UserDto userDto);

    /**
     * This method pays for the order and updates the order status to 'PAID'
     * @param orderDto order to be paid
     * @param cardDto card for payment
     * @throws CardNotFoundException if the user does not have such a card
     */
    void payOrder(OrderDto orderDto, CardDto cardDto);

    /**
     * Returns a list all orders
     * @return list of orders
     */
    List<OrderDto> findAll();

    /**
     * This method updates the state of the order to 'DELIVERED'
     * @param id id order for update
     * @return an updated order
     */
    OrderDto setDelivered(Integer id);
}
