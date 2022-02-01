package org.academy.OnlineStoreDemo.valid;

import lombok.AllArgsConstructor;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductValidatorService {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    public String validateProduct(ProductDto productDto, String productCategoryName) {
        String message = "";
        if (productDto.getName().trim().equals("")) {
            message = "emptyProductName";
        }
        if (productDto.getPrice() == null) {
            message = "emptyPriceProduct";
        }
        if (productDto.getAmount() == null) {
            message = "emptyAmountProduct";
        }
        if (Boolean.FALSE.equals(productCategoryService.existsProductCategoryByName(productCategoryName.trim()))) {
            message = "categoryNotExist";
        }
        return message;
    }

    public String validateSearchProduct(String parameter, String name) {
        String message = "";
        if (parameter.equals("category")) {
            ProductCategoryDto productCategoryDto = productCategoryService.findCategoryByName(name.trim());
            if (productCategoryDto.getId()==null) {
                message = "categoryNotFound";
            }
            if (productCategoryDto.getProductsDto().isEmpty()) {
                message = "emptyCategory";
            }
        }
        if (parameter.equals("nameProd")) {
            List<ProductDto> productsDto = productService.findAllByName(name.trim());
            if (productsDto.isEmpty()) {
                message = "productNotFound";
            }
        }
        return message;
    }
}
