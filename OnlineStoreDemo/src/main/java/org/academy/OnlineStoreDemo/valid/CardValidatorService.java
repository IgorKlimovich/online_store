package org.academy.OnlineStoreDemo.valid;

import org.academy.OnlineStoreDemo.dto.CardDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.academy.OnlineStoreDemo.constants.Constants.EXIST_CARD;

@Service
public class CardValidatorService {

    public String validateCard(List<CardDto> cardsDto, CardDto cardDto){
        String message= null;
         if (cardsDto.stream().anyMatch(item -> item.getNumber().equals(cardDto.getNumber()))) {
             message=EXIST_CARD;
        }
         return message;
    }
}
