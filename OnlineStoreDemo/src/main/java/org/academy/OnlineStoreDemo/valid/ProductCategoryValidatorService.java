package org.academy.OnlineStoreDemo.valid;

import org.academy.OnlineStoreDemo.service.ProductCategoryService;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryValidatorService {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryValidatorService(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    public String validateProductCategory(String name) {
        String message="";
        if (name.trim().equals("")){
            message="emptyCategoryName";
        }
        if (Boolean.TRUE.equals(productCategoryService.existsProductCategoryByName(name.trim()))){
            message="existCategory";
        }
        return message;
    }
}
