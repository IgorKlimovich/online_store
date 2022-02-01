package org.academy.OnlineStoreDemo.valid;

import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.academy.OnlineStoreDemo.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchValidatorService {

    public String validateSearch(String name, List<ProductDto> filteredProductsDto){
        String message="notEmpty";
        if(filteredProductsDto.isEmpty()||name.trim().equals("")){
            message="empty";
        }
        return message;
    }

    public String validateCategory(List<ProductDto> productsDto) {
        String message="notEmpty";
        if (productsDto.isEmpty()){
            message="empty";
        }
        return message;
    }
}
