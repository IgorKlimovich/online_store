package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.academy.OnlineStoreDemo.model.entity.OrderProduct;
import org.academy.OnlineStoreDemo.model.entity.StateOrder;
import org.academy.OnlineStoreDemo.model.entity.User;


import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Integer id;

    private Date date;

    private Double fullPrice;

    private UserDto userDto;

    private StateOrderDto stateOrderDto;

    private List<OrderProductDto> orderProductsDto ;

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", date=" + date +
                ", fullPrice=" + fullPrice +
                ", userDto name=" + userDto.getFirstName() +
                ", stateOrderDto name=" + stateOrderDto.getName() +
                '}';
    }
}
