package org.academy.OnlineStoreDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    private String namePhoto;

    private ProductCategoryDto productCategoryDto;

    private List<OrderProductDto> orderProductsDto;

    public String getPhotosImagePath() {
        if (namePhoto == null || id == null) return null;
        return "/product-photos/" + id + "/" + namePhoto;
    }


    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isExist=" + isExist +
                ", price=" + price +
                ", amount=" + amount +
                ", namePhoto=" + namePhoto +
                '}';
    }
}
