package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDto {

    private Integer id;

    private String name;

    private Integer amount;

    private List<ProductDto> productsDto;
}
