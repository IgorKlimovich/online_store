package org.academy.OnlineStoreDemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderProductDto {

    private Integer id;

    private OrderDto orderDto;

    private ProductDto productDto;

    private Double newProductPrice;

}
