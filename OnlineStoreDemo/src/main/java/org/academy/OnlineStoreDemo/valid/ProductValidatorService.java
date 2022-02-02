package org.academy.OnlineStoreDemo.valid;

import lombok.AllArgsConstructor;
import org.academy.OnlineStoreDemo.dto.ProductCategoryDto;
import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.academy.OnlineStoreDemo.constants.Constants.*;

@Service
@AllArgsConstructor
public class ProductValidatorService {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    public String validateProduct(ProductDto productDto, String productCategoryName) {
        String message = "";
        if (productDto.getName().trim().equals("")) {
            message = EMPTY_PRODUCT_NAME;
        }
        if (productDto.getPrice() == null) {
            message = EMPTY_PRICE_PRODUCT;
        }
        if (productDto.getAmount() == null) {
            message = EMPTY_AMOUNT_PRODUCT;
        }
        if (Boolean.FALSE.equals(productCategoryService.existsProductCategoryByName(productCategoryName.trim()))) {
            message = CATEGORY_NOT_EXIST;
        }
        return message;
    }

    public String validateSearchProduct(String parameter, String name) {
        String message = "";
        if (parameter.equals(CATEGORY)) {
            if (Boolean.FALSE.equals(productCategoryService.existsProductCategoryByName(name))){
                message = CATEGORY_NOT_FOUND;
            }
            ProductCategoryDto productCategoryDto = productCategoryService.findCategoryByName(name.trim());
            if (productCategoryDto.getId()!=null&&productCategoryDto.getProductsDto().isEmpty()){
                message = EMPTY_CATEGORY;
            }
        }
        if (parameter.equals(NAME_PROD)) {
            List<ProductDto> productsDto = productService.findAllByName(name.trim());
            if (productsDto.isEmpty()) {
                message = PRODUCT_NOT_FOUND;
            }
        }
        return message;
    }
}
