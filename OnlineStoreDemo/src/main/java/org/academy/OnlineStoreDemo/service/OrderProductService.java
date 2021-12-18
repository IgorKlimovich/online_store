package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.model.entity.Order;
import org.academy.OnlineStoreDemo.model.entity.OrderProduct;
import org.academy.OnlineStoreDemo.model.entity.Product;
import org.academy.OnlineStoreDemo.model.entity.User;

import java.util.List;


public interface OrderProductService {
    void save(OrderProduct orderProduct);

    void removeProductFromOrder(Order order, ProductDto productDto);

    void saveProductToOrder(Order order, ProductDto productDto);

    List<OrderProduct> findByOrderId(Integer id);
}
