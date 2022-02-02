package org.academy.OnlineStoreDemo.valid;

import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.springframework.stereotype.Service;

import static org.academy.OnlineStoreDemo.constants.Constants.EMPTY_CATEGORY_NAME;
import static org.academy.OnlineStoreDemo.constants.Constants.EXIST_CATEGORY;

@Service
public class ProductCategoryValidatorService {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryValidatorService(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    public String validateProductCategory(String name) {
        String message = "";
        if (name.trim().equals("")) {
            message = EMPTY_CATEGORY_NAME;
        }
        if (Boolean.TRUE.equals(productCategoryService.existsProductCategoryByName(name.trim()))) {
            message = EXIST_CATEGORY;
        }
        return message;
    }
}
