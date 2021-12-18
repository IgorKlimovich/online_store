package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import org.academy.OnlineStoreDemo.model.entity.Card;
import org.academy.OnlineStoreDemo.model.entity.User;

import java.util.List;

public interface CardService {
    void save(CardDto cardDto, UserDto userDto);
    CardDto findById(Integer cardId);
    void remove(CardDto cardDto);
    void update(CardDto cardDto);
    List<CardDto> findAllByUser(UserDto userDto);
}
