package org.academy.OnlineStoreDemo.valid;

import org.academy.OnlineStoreDemo.dto.CardDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardValidatorService {

    public String validateCard(List<CardDto> cardsDto, CardDto cardDto){
        String message= null;
         if (cardsDto.stream().anyMatch(item -> item.getNumber().equals(cardDto.getNumber()))) {
             message="existCard";
        }
         return message;
    }
}
