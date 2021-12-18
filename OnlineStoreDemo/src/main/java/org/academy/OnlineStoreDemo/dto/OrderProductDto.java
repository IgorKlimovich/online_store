package org.academy.OnlineStoreDemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.academy.OnlineStoreDemo.model.entity.Order;
import org.academy.OnlineStoreDemo.model.entity.Product;

@Data
@NoArgsConstructor
public class OrderProductDto {

    Integer id;

    private Order order;

    private Product product;

    private Double productPrice;
}
