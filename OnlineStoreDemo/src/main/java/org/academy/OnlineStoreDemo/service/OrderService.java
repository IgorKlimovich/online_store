package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.model.entity.Order;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.User;

import java.util.List;

public interface OrderService {
    //    void saveProductToOrder(User user, Product product);
    Order findById(Integer id);

    Order createOrderIfNotActive(User user);

    void payOrder(Order order, Card card);

    List<Order> findAll();

    void setDelivered(Order order);
}
