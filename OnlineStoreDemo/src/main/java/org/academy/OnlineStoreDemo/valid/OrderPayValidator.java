package org.academy.OnlineStoreDemo.valid;

import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.OrderDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class OrderPayValidator {

    public Integer validatePay(UserDto userDto, CardDto cardDto, OrderDto orderFromDbDto){
        Integer message =null;

        if (userDto.getCardsDto().stream().noneMatch(item -> item.getNumber().equals(cardDto.getNumber()))) {
         return 6;
        }
        if (userDto.getCardsDto().stream().filter(item -> item.getNumber().equals(cardDto.getNumber()))
                .findFirst()
                .orElse(new CardDto()).getTotalAmount() < orderFromDbDto.getFullPrice()) {
            return 7;
        }
        return message;
    }
}
