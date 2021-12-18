package org.academy.OnlineStoreDemo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.academy.OnlineStoreDemo.model.entity.OrderProduct;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Integer id;

    private String name;

    private String description;

    private Boolean isExist;

    private Double price;

    private Integer amount;

    private ProductCategoryDto productCategoryDto;

    private List<OrderProductDto> orderProductsDto;
    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isExist=" + isExist +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
