package org.academy.OnlineStoreDemo.valid;

import org.academy.OnlineStoreDemo.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.academy.OnlineStoreDemo.constants.Constants.EMPTY;
import static org.academy.OnlineStoreDemo.constants.Constants.NOT_EMPTY;

@Service
public class SearchValidatorService {

    public String validateSearch(String name, List<ProductDto> filteredProductsDto){
        String message=NOT_EMPTY;
        if(filteredProductsDto.isEmpty()||name.trim().equals("")){
            message= EMPTY;
        }
        return message;
    }

    public String validateCategory(List<ProductDto> productsDto) {
        String message=NOT_EMPTY;
        if (productsDto.isEmpty()){
            message= EMPTY;
        }
        return message;
    }
}
