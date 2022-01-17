package org.academy.OnlineStoreDemo.service;


import org.academy.OnlineStoreDemo.dto.CardDto;
import org.academy.OnlineStoreDemo.dto.UserDto;
import java.util.List;

public interface CardService {
    void save(CardDto cardDto, UserDto userDto);
    CardDto findById(Integer cardId);
    void remove(Integer CardId);
    void update(CardDto cardDto);
    List<CardDto> findAllByUser(UserDto userDto);
}
